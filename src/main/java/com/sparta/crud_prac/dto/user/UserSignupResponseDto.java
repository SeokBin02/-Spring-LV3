package com.sparta.crud_prac.dto.user;


import lombok.Getter;

@Getter
public class UserSignupResponseDto {
    private String message;
    private int status;

    public UserSignupResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
