package com.sparta.crud_prac.entity;


import com.sparta.crud_prac.dto.post.PostCURequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Post extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    // cascase = 게시글이 삭제되면 해당 게시글에 달려있던 댓글까지 같이 소멸.
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public Post(PostCURequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.username = user.getUsername();
        this.content = requestDto.getContent();
        setUser(user);
    }

    private void setUser(User user){
        this.user = user;
    }

    public void update(PostCURequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
