package com.sparta.crud_prac.dto.comment;
import com.sparta.crud_prac.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private int heartCount;

    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.heartCount = comment.getHeartCount();
    }
}
