package com.sparta.crud_prac.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "HEART_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    public Heart(User user, Post post) {
        setUser(user);
        setPost(post);
    }

    public Heart(User user, Comment comment) {
        setUser(user);
        setComment(comment);
    }

    private void setUser(User user){
        this.user = user;
    }

    private void setPost(Post post){
        this.post = post;

        if(!post.getHearts().contains(this))
            post.getHearts().add(this);
    }

    private void setComment(Comment comment){
        this.comment = comment;

        if(!comment.getHearts().contains(this))
            comment.getHearts().add(this);
    }
}
