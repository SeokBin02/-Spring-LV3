package com.sparta.crud_prac.repository;

import com.sparta.crud_prac.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
