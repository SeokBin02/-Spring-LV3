package com.sparta.crud_prac.service;


import com.sparta.crud_prac.dto.post.PostCURequestDto;
import com.sparta.crud_prac.dto.post.PostDeleteResponseDto;
import com.sparta.crud_prac.dto.post.PostResponseDto;
import com.sparta.crud_prac.entity.Post;
import com.sparta.crud_prac.entity.User;
import com.sparta.crud_prac.jwt.JwtUtil;
import com.sparta.crud_prac.repository.CommentRepository;
import com.sparta.crud_prac.repository.PostRepository;
import com.sparta.crud_prac.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService extends SuperService {
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, JwtUtil jwtUtil) {
        super(postRepository, userRepository, commentRepository, jwtUtil);
    }

    // 전체 게시글 목록 조회 API
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    //    2. 게시글 작성 API
    @Transactional
    public PostResponseDto createPost(PostCURequestDto requestDto, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);                  // 토큰 유효성 검사를 거친 후 user 정보를 가져옴
        Post post = new Post(requestDto, user);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //    3. 선택한 게시글 조회 API
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        // 게시글 존재 유무 체크 후 리턴
        return new PostResponseDto(validateIsPosts(id));
    }


    //    4. 선택한 게시글 수정 API
    @Transactional
    public PostResponseDto updatePost(Long id, PostCURequestDto requestDto, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);      // 토큰 유효성 검사를 거친 후 user 정보를 가져옴
        Post post = validateIsPosts(id);                 // 조회하려는 게시판의 존재 유무 체크
        isCheckAuthorFromPosts(user, post);                      // 해당 게시판의 수정 권한 유무 체크
        post.update(requestDto);                        // 게시글 수정
        return new PostResponseDto(post);
    }


    //    5. 선택한 게시글 삭제 API
    @Transactional
    public PostDeleteResponseDto deletePost(Long id, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);      // 토큰 유효성 검사를 거친 후 user 정보를 가져옴
        Post post = validateIsPosts(id);                 // 조회하려는 게시판의 존재 유무 체크
        isCheckAuthorFromPosts(user, post);                      // 해당 게시판의 삭제 권한 유무 체크
        postRepository.delete(post);                    // 게시글 삭제
        return new PostDeleteResponseDto("삭제에 성공하였습니다.", HttpStatus.OK.value());
    }
}
