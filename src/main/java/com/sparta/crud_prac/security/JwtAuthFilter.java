package com.sparta.crud_prac.security;

import com.sparta.crud_prac.entity.ExceptionEnum;
import com.sparta.crud_prac.exception.customException.CustomException;
import com.sparta.crud_prac.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@WebFilter(urlPatterns = "/post/posts/*")
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("request.getRequestURI() = " + request.getRequestURI());

        String token = jwtUtil.resolveToken(request);   // request로 부터 token을 받아옴


        if(token != null ){
            if(!jwtUtil.validateToken(token)){
                // 토큰이 유효하지 않으면 예외 처리
                throw new CustomException(ExceptionEnum.INVALID_TOKEN);
            }
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(claims.getSubject());
        }
        filterChain.doFilter(request,response);
    }

    // 인증 객체 생성 및 등록
    private void setAuthentication(String username){
        SecurityContext context = SecurityContextHolder.createEmptyContext();       // SecurityContextHolder에서 빈 SecurityContext 객체 생성
        Authentication authentication = jwtUtil.createAuthentication(username);     // 인증에 성공한 user의 인증 객체 생성
        context.setAuthentication(authentication);                                  // 인증객체(Authentication)를 context에 set
        SecurityContextHolder.setContext(context);                                  // SecurityContext를 SecurityContextHolder에 set
    }
}
