package com.sparta.crud_prac.dto.post;

import com.sparta.crud_prac.entity.Comment;
import com.sparta.crud_prac.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Getter
public class PostResponseDto {
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Comment> comments = new ArrayList<>();
//    private List<CommentDto>
    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.comments = post.getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getId).reversed())
                .map(Comment::new)
                .collect(Collectors.toList());
    }
}
