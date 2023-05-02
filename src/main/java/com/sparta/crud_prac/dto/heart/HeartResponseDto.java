package com.sparta.crud_prac.dto.heart;

import lombok.Getter;

@Getter
public class HeartResponseDto {
    private String message;

    public HeartResponseDto(String message) {
        this.message = message;
    }
}
