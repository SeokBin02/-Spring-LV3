package com.sparta.crud_prac.dto.comment;

import lombok.Getter;

@Getter
public class CommentDeleteResponseDto {
    private String message;
    private int status;

    public CommentDeleteResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
