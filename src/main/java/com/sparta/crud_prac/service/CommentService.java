package com.sparta.crud_prac.service;

import com.sparta.crud_prac.dto.comment.CommentDeleteResponseDto;
import com.sparta.crud_prac.dto.comment.CommentRequestDto;
import com.sparta.crud_prac.dto.comment.CommentResponseDto;
import com.sparta.crud_prac.entity.Comment;
import com.sparta.crud_prac.entity.Post;
import com.sparta.crud_prac.entity.User;
import com.sparta.crud_prac.jwt.JwtUtil;
import com.sparta.crud_prac.repository.CommentRepository;
import com.sparta.crud_prac.repository.HeartRepository;
import com.sparta.crud_prac.repository.PostRepository;
import com.sparta.crud_prac.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService extends SuperService {
    @Autowired
    public CommentService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, HeartRepository heartRepository, JwtUtil jwtUtil) {
        super(postRepository, userRepository, commentRepository, heartRepository, jwtUtil);
    }

    // 댓글 작성 API
    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);               // 토큰 유효성 검사
        Post post = validateIsPosts(id);                         // 게시글 존재 유무 검사
        Comment comment = new Comment(requestDto, user, post);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    // 댓글 수정 API
    @Transactional
    public CommentResponseDto updateCommnet(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);                  // 토큰 유효성 검사
        Comment comment = validateIsComments(id);                   // 댓글 유무 확인
        isCheckAuthorFromComments(user, comment);                   // 작성자 일치 여부 체크
        comment.update(requestDto);                                 // 댓글 수정 및 dirty checking
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제 API
    @Transactional
    public CommentDeleteResponseDto deleteComment(Long id, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);                  // 토큰의 유효성 검사
        Comment comment = validateIsComments(id);                   // 선택한 댓글의 DB 저장 유무 확인
        isCheckAuthorFromComments(user, comment);                   // 댓글 작성자 일치 여부 체크
        commentRepository.delete(comment);                          // 댓글 삭제
        return new CommentDeleteResponseDto("삭제를 성공했습니다.", HttpStatus.OK.value());
    }
}
