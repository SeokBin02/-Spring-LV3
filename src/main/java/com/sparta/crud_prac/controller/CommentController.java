package com.sparta.crud_prac.controller;

import com.sparta.crud_prac.dto.comment.CommentDeleteResponseDto;
import com.sparta.crud_prac.dto.comment.CommentRequestDto;
import com.sparta.crud_prac.dto.comment.CommentResponseDto;
import com.sparta.crud_prac.entity.Comment;
import com.sparta.crud_prac.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성 API
    @PostMapping("/comments/{id}")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody  CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.createComment(id, requestDto, request);
    }
    // 댓글 수정 API
    @PutMapping("/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        return commentService.updateCommnet(id, requestDto, request);
    }
    // 댓글 삭제 API
    @DeleteMapping("/comments/{id}")
    public CommentDeleteResponseDto deleteComment(@PathVariable Long id, HttpServletRequest request){
        return commentService.deleteComment(id, request);
    }
}
