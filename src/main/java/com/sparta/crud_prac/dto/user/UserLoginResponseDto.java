package com.sparta.crud_prac.dto.user;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private String message;
    private int status;

    public UserLoginResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
