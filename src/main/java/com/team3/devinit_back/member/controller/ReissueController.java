package com.team3.devinit_back.member.controller;

import com.team3.devinit_back.member.entity.RefreshEntity;
import com.team3.devinit_back.member.jwt.JWTUtil;
import com.team3.devinit_back.member.repository.MemberRepository;
import com.team3.devinit_back.member.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final MemberRepository memberRepository;

    @Operation(summary = "토큰 재발급",description = "refresh토큰을 확인하고  access 토큰과 refresh토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String socialId = jwtUtil.getSocialId(refresh);
        String role = jwtUtil.getRole(refresh);
        String memberId = memberRepository.findBySocialId(socialId).getId();
        String newAccess = jwtUtil.createJwt("access", socialId, role, 86400000L);
        String newRefresh = jwtUtil.createJwt("refresh", socialId, role,86400000L); // refresh rotate

        response.setHeader("access", newAccess);
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(socialId, newRefresh, 86400000L);

        response.addCookie(createCookie("refresh", newRefresh));
        return ResponseEntity.ok(memberId);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
    private void addRefreshEntity(String socialId, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setSocialId(socialId);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

}
