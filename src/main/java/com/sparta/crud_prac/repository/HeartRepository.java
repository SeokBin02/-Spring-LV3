package com.sparta.crud_prac.repository;

import com.sparta.crud_prac.entity.Comment;
import com.sparta.crud_prac.entity.Heart;
import com.sparta.crud_prac.entity.Post;
import com.sparta.crud_prac.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByUserAndPost(User user, Post post);
    Optional<Heart> findByUserAndComment(User user, Comment comment);
}
