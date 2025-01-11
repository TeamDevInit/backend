package com.team3.devinit_back.member.jwt;

import com.team3.devinit_back.member.dto.CustomOAuth2User;
import com.team3.devinit_back.member.dto.MemberDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

//스프링 시큐리티 filter chain에 요청에 담긴 JWT를 검증하기 위한 필터클래스
//요청 쿠키에 JWT존재하면 검증후 강제로 시큐리티 컨테스트 홀더 세션(임시) 생성
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    public  JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil= jwtUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = null;
        String accessToken = request.getHeader("access");
        //토큰이 없으면 다음 필터로 넘김
        if(accessToken == null){
            filterChain.doFilter(request, response);
            return;
        }
        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰에서 username과 role 획득
        String socialId = jwtUtil.getSocialId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //MEMBERDTO를 생성하여 값 set
        MemberDto memberDto = new MemberDto();
        memberDto.setName(socialId);
        memberDto.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(memberDto);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);


    }
}
