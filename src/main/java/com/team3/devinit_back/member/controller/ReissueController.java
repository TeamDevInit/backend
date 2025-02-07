package com.team3.devinit_back.member.controller;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.jwt.JWTUtil;
import com.team3.devinit_back.member.service.MemberService;
import com.team3.devinit_back.member.service.RedisTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final RedisTokenService redisTokenService;


    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급",description = "refresh토큰을 확인하고  access 토큰과 refresh토큰을 재발급합니다.")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = extractRefreshTokenFromCookies(request.getCookies());
        validateRefreshToken(refresh);

        String socialId = jwtUtil.getSocialId(refresh);
        String role = jwtUtil.getRole(refresh);
        String memberId = memberService.findMemberBySocialId(socialId).getId();
        String newAccess = jwtUtil.createJwt("access", socialId, role, 86400000L);
        String newRefresh = jwtUtil.createJwt("refresh", socialId, role,86400000L);

        redisTokenService.deleteRefreshToken(socialId);
        redisTokenService.saveRefreshToken(socialId, newRefresh, 86400000L);

        setResponseTokens(response, newAccess, newRefresh);

        return ResponseEntity.ok(memberId);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private String extractRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new CustomException(ErrorCode.EMPTY_REFRESH_TOKEN);
        }

        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        throw new CustomException(ErrorCode.EMPTY_REFRESH_TOKEN);
    }

    private void validateRefreshToken(String refresh) {
        if (refresh == null) {
            throw new CustomException(ErrorCode.EMPTY_REFRESH_TOKEN);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private void setResponseTokens(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setHeader("access", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
    }
}
