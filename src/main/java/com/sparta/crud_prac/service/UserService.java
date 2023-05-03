package com.sparta.crud_prac.service;

import com.sparta.crud_prac.dto.user.UserLoginRequestDto;
import com.sparta.crud_prac.dto.user.UserLoginResponseDto;
import com.sparta.crud_prac.dto.user.UserSignupRequestDto;
import com.sparta.crud_prac.dto.user.UserSignupResponseDto;
import com.sparta.crud_prac.entity.User;
import com.sparta.crud_prac.entity.UserRoleEnum;
import com.sparta.crud_prac.jwt.JwtUtil;
import com.sparta.crud_prac.repository.CommentRepository;
import com.sparta.crud_prac.repository.HeartRepository;
import com.sparta.crud_prac.repository.PostRepository;
import com.sparta.crud_prac.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends SuperService {
    @Autowired
    public UserService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, HeartRepository heartRepository, JwtUtil jwtUtil) {
        super(postRepository, userRepository, commentRepository, heartRepository, jwtUtil);
    }

    // 회원가입 서비스 메서드
    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {
        validateIsDuplicate(requestDto.getUsername());              // 아이디 중복 체크
        UserRoleEnum role = isAdmin(requestDto);                    // 가입자의 권한 체크
        User user = new User(requestDto, role);
        User.inputIsMatches(user.getUsername(), user.getPassword()); // 입력 조건 정규식 체크
        userRepository.save(new User(requestDto, role));
        return new UserSignupResponseDto("회원가입에 성공하셨습니다!", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLoginRequestDto requestDto, HttpServletResponse response) {
        User user = validateIsUsers(requestDto.getUsername());       // 아이디 존재 유무 체크
        validatePassword(requestDto.getPassword(), user);           // 비밀번호 체크
        // username 과 email로 토큰을 생성해서 Authorization_Header와 함께 response에 HEADER를 날린다.
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        return new UserLoginResponseDto("로그인에 성공하셨습니다!", HttpStatus.OK.value());
    }
}
