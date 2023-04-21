package com.sparta.crud_prac.service;


import com.sparta.crud_prac.dto.PostCURequestDto;
import com.sparta.crud_prac.dto.PostDeleteResponseDto;
import com.sparta.crud_prac.dto.PostResponseDto;
import com.sparta.crud_prac.entity.Post;
import com.sparta.crud_prac.entity.User;
import com.sparta.crud_prac.jwt.JwtUtil;
import com.sparta.crud_prac.repository.PostRepository;
import com.sparta.crud_prac.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    // 전체 게시글 목록 조회 API
    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }


//    2. 게시글 작성 API
//    - 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능
//    - 제목, 작성 내용을 저장하고
//    - 저장된 게시글을 Client 로 반환하기(username은 로그인 된 사용자)
    @Transactional
    public PostResponseDto createPost(PostCURequestDto requestDto, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);
        if(user != null){
            Post post = new Post(requestDto, user.getId(), user.getUsername());
            postRepository.save(post);
            return new PostResponseDto(post);
        } else{
            return null;
        }
    }

//    3. 선택한 게시글 조회 API
//    - 선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기
//    (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }


    // 로그인시 생성된 토큰을 꺼내와서 유효성검사 후 해당 토큰의 user를 반환
    private User getUserInfoFromToken (HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);       // request에서 토큰정보를 뺴와서 String token변수에 저장해놓는다.
        Claims claims;
//1. 신분증있니?
//        2. 신분증이 유효한지
        if (token != null) { // 토큰이 비어있지 않다면
            if (jwtUtil.validateToken(token)) { // 토큰의 유효성 검사
                claims = jwtUtil.getUserInfoFromToken(token); // 토큰으로부터 유저의 정보를 방아옴.
            } else {
                throw new IllegalArgumentException("Token Error");
            }

//            Optional<User> user = userRepository.findByUsername(claims.getSubject());

            // 토큰 값(신분증) 으로 (담배사렬는 애의 정보)User Entity 초기화
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new NullPointerException("유저 정보가 존재하지 않습니다.")
            );

            return user;
        }else{
            return null;
        }
    }

//    4. 선택한 게시글 수정 API
//    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
//    - 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    @Transactional
    public PostResponseDto updatePost(Long id, PostCURequestDto requestDto, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);
//        api 실행할때 클라이언트가 토큰을 제출하고 서버에서는 그 토큰이 유효한지를 검사해서 그 토큰 내부에 저장되어있는 유저정보를 토대로
//                로그인 한 유저의 정보를 받아온다.

        if(user != null){
            // 해당 유저가 작성한 게시글을 가져옴

            Post post = postRepository.findByIdAndUserid(id, user.getId()).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            // 입력받은 제목, 작성 내용으로 수정
            post.update(requestDto);

            return new PostResponseDto(post);
        } else{
            return null;
        }
    }



//    5. 선택한 게시글 삭제 API
//            - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능
//    - 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    @Transactional
    public PostDeleteResponseDto deletePost(Long id, HttpServletRequest request) {
        User user = getUserInfoFromToken(request);
        if(user != null){
            // 삭제할 게시글 조회
            postRepository.deleteByIdAndUserid(id, user.getId()).orElseThrow(
                    () -> new NullPointerException("삭제할 게시글이 존재하지 않습니다.")
            );
            return new PostDeleteResponseDto("삭제에 성공하였습니다.", HttpStatus.OK.value());
        } else{
            return new PostDeleteResponseDto("삭제에 실패하였습니다.", HttpStatus.NOT_FOUND.value());
        }
    }



}
