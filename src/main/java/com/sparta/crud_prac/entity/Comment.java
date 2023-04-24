package com.sparta.crud_prac.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.crud_prac.dto.comment.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"post", "user"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    public Comment(CommentRequestDto requestDto, User user, Post post) {
        this.content = requestDto.getContent();
        setUser(user);
        setPost(post);
    }

    public Comment(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.user = comment.getUser();
        this.post = comment.getPost();
    }

    private void setUser(User user){
        this.user = user;
    }

    private void setPost(Post post){
        this.post = post;

        if(post.getComments().contains(this))
            post.getComments().add(this);
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
