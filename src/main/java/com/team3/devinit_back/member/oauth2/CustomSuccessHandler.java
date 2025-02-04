package com.team3.devinit_back.member.oauth2;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.jwt.JWTUtil;
import com.team3.devinit_back.member.service.RedisTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String socialId = customUserDetails.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        String refreshToken = jwtUtil.createJwt("refresh",socialId, role, 86400000L);

        addRefreshEntity(socialId, refreshToken, 86400000L);

        // refresh -> 쿠키, access -> 헤더? -> 하이퍼링크에서 넘어오는거라 바로 못줌
        response.addCookie(createCookie("refresh", refreshToken));
        response.sendRedirect("http://34.64.72.48"); //  ->  http://34.64.44.54
        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);  //HTTPS 변경
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
    private void addRefreshEntity(String socialId, String refresh, long  expiredMs) {
        redisTokenService.saveRefreshToken(socialId, refresh, expiredMs);
    }
}
