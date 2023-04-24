package com.sparta.crud_prac.dto.post;

import lombok.Getter;


// 게시글 작성, 수정할 때 정보를 받아오는 DTO
@Getter
public class PostCURequestDto {
    private String title;
    private String content;
}
