package com.sparta.crud_prac.service;

import com.sparta.crud_prac.dto.user.UserSignupRequestDto;
import com.sparta.crud_prac.entity.*;
import com.sparta.crud_prac.exception.customException.CustomException;
import com.sparta.crud_prac.jwt.JwtUtil;
import com.sparta.crud_prac.repository.CommentRepository;
import com.sparta.crud_prac.repository.HeartRepository;
import com.sparta.crud_prac.repository.PostRepository;
import com.sparta.crud_prac.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SuperService {
    protected final PostRepository postRepository;
    protected final UserRepository userRepository;
    protected final CommentRepository commentRepository;
    protected final HeartRepository heartRepository;
    protected final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 로그인시 생성된 토큰을 꺼내와서 유효성검사 후 해당 토큰의 user를 반환
    protected User getUserInfoFromToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);       // request에서 토큰정보를 뺴와서 String token변수에 저장해놓는다.
        //토큰 유효성 체크
        validateIsToken(token);
        // 토큰으로부터 유저의 정보를 방아옴.
        return validateIsUsers(jwtUtil.getUserInfoFromToken(token).getSubject());
    }

    // 토큰 존재 여부 체크
    protected void validateIsToken(String token){
        if(token == null || !jwtUtil.validateToken(token))
            throw new CustomException(ExceptionEnum.TOKEN_NOT_FOUND);
    }

    // 회원 존재 유무
    protected User validateIsUsers(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ExceptionEnum.USERS_NOT_FOUND)
        );
    }

    // 게시글 존재 유무 체크
    protected Post validateIsPosts(Long id){
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(ExceptionEnum.POSTS_NOT_FOUND)
        );
    }

    // 댓글 존재 유무 체크
    protected Comment validateIsComments(Long id){
        return commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ExceptionEnum.COMMENTS_NOT_FOUND)
        );
    }

    // 게시글 작성자 일치 여부 체크
    protected void isCheckAuthorFromPosts(User user, Post post){
        if(!(post.getUser().getId() == user.getId())){
            if(user.getRole() == UserRoleEnum.ADMIN) return;
            throw new CustomException(ExceptionEnum.NOT_ALLOW_AUTHORIZATIONS);
        }
    }

    // 댓글 작성자 일치 여부 체크
    protected void isCheckAuthorFromComments(User user, Comment comment){
        if(!(comment.getUser().getId() == user.getId())){
            if(user.getRole() == UserRoleEnum.ADMIN) return;
            throw new CustomException(ExceptionEnum.NOT_ALLOW_AUTHORIZATIONS);
        }
    }

    // 아이디 중복 체크
    protected void validateIsDuplicate(String username) {
        if(userRepository.findByUsername(username).isPresent())
            throw new CustomException(ExceptionEnum.USERS_DUPLICATION);
    }

    // 비밀번호 체크
    protected void validatePassword(String pw, User user){
        if(!user.getPassword().equals(pw))
            throw new CustomException(ExceptionEnum.INVALID_PASSWORD);
    }

    // 관리자 체크
    protected UserRoleEnum isAdmin(UserSignupRequestDto requestDto) {
        if (requestDto.isAdmin()) {        // admin에 체크 했다면
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(ExceptionEnum.INVALID_INPUT);
            } else {
                return UserRoleEnum.ADMIN;
            }
        } else {
            return UserRoleEnum.USER;
        }
    }
}
