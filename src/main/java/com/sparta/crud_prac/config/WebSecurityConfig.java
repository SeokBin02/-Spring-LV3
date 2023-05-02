package com.sparta.crud_prac.config;

import com.sparta.crud_prac.jwt.JwtUtil;
import com.sparta.crud_prac.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;

    // password 암호화
    // @EnableWebSecurity 어노테이션 설정해서 스프링 Security 지원이 가능하게 해야
    // PasswordEncoder가 import 가능하다.
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }


    // h2console, commonlocations 인증 절차 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())                                 //h2db 인증 무시
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());      //staticResource(images,js,css등등) 인증 절차 무시
    }

    // filter chain 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests()
                .requestMatchers("/users/**").permitAll()      // /users로 들어오는 URL 요청은 인증 없이 허가함
                .requestMatchers("/commnet/**").permitAll()
                .requestMatchers("/post/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);                // 그외 URL 요청은 인증 절차를 밟겠음.

        return http.build();
    }

}
