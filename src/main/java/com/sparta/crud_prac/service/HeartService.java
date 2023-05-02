package com.sparta.crud_prac.service;

import com.sparta.crud_prac.dto.comment.CommentRequestDto;
import com.sparta.crud_prac.dto.comment.CommentResponseDto;
import com.sparta.crud_prac.dto.heart.HeartResponseDto;
import com.sparta.crud_prac.entity.Comment;
import com.sparta.crud_prac.entity.Heart;
import com.sparta.crud_prac.entity.Post;
import com.sparta.crud_prac.entity.User;
import com.sparta.crud_prac.jwt.JwtUtil;
import com.sparta.crud_prac.repository.CommentRepository;
import com.sparta.crud_prac.repository.HeartRepository;
import com.sparta.crud_prac.repository.PostRepository;
import com.sparta.crud_prac.repository.UserRepository;
import com.sparta.crud_prac.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service

public class HeartService extends SuperService{
    @Autowired
    public HeartService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, HeartRepository heartRepository, JwtUtil jwtUtil) {
        super(postRepository, userRepository, commentRepository, heartRepository, jwtUtil);
    }

    @Transactional
    public HeartResponseDto heartPost(Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Post post = validateIsPosts(id);

        // Authentication의 user가 게시글(id)에 좋아요 를 한적이 있는지 유무를 검색.
        Optional<Heart> byUserAndPost = heartRepository.findByUserAndPost(user, post);

        // 만약 사용자가 해당 게시글을 좋아요 한 상태라면 좋아요 취소
        if(byUserAndPost.isPresent()){
            heartRepository.delete(byUserAndPost.get());
            post.decreseHeartCount();
            return new HeartResponseDto("좋아요를 취소하셨습니다.");
        }
        else{   // 좋아요를 한적이 없다면 좋아요 생성 후 저장
            heartRepository.save(new Heart(user, post));
            post.increseHeartCount();
            return new HeartResponseDto("해당 게시글을 좋아합니다.");
        }
    }

    @Transactional
    public HeartResponseDto heartComment(Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Comment comment = validateIsComments(id);

        // Authentication의 user가 게시글(id)에 좋아요 를 한적이 있는지 유무를 검색.
        Optional<Heart> byUserAndComment = heartRepository.findByUserAndComment(user, comment);

        // 만약 사용자가 해당 게시글을 좋아요 한 상태라면 좋아요 취소
        if(byUserAndComment.isPresent()){
            heartRepository.delete(byUserAndComment.get());
            comment.decreseHeartCount();
            return new HeartResponseDto("좋아요를 취소하셨습니다.");
        }
        else{   // 좋아요를 한적이 없다면 좋아요 생성 후 저장
            heartRepository.save(new Heart(user, comment));
            comment.increseHeartCount();
            return new HeartResponseDto("해당 댓글을 좋아합니다.");
        }
    }

    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);               // 토큰 유효성 검사
        Post post = validateIsPosts(id);                         // 게시글 존재 유무 검사
        Comment comment = new Comment(requestDto, user, post);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }
}
