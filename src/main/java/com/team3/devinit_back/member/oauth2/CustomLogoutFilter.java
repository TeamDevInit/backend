package com.team3.devinit_back.member.oauth2;

import com.team3.devinit_back.global.exception.CustomException;
import com.team3.devinit_back.global.exception.ErrorCode;
import com.team3.devinit_back.member.jwt.JWTUtil;
import com.team3.devinit_back.member.repository.RefreshRepository;
import com.team3.devinit_back.member.service.RedisTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final RedisTokenService redisTokenService;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!isLogoutRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = extractRefreshTokenFromCookies(request.getCookies());
        validateRefreshToken(refresh);

        String socialId = jwtUtil.getSocialId(refresh);
        redisTokenService.deleteRefreshToken(socialId);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/logout") && request.getMethod().equalsIgnoreCase("POST");
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

}