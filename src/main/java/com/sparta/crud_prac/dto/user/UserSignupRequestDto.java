package com.sparta.crud_prac.dto.user;

import com.sparta.crud_prac.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {
    // ID
    private String username;
    // PW
    private String password;
    // EMAIL
    private String email;
    // 권한 체크 논리 값
    private boolean admin = false;
    // 권한 인가 토큰
    private String adminToken = "";
}
