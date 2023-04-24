package com.sparta.crud_prac.controller;


import com.sparta.crud_prac.dto.user.UserLoginRequestDto;
import com.sparta.crud_prac.dto.user.UserLoginResponseDto;
import com.sparta.crud_prac.dto.user.UserSignupRequestDto;
import com.sparta.crud_prac.dto.user.UserSignupResponseDto;
import com.sparta.crud_prac.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public UserSignupResponseDto signup(@RequestBody UserSignupRequestDto requestDto){
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto, HttpServletResponse response){
        return userService.login(requestDto, response);
    }
}
