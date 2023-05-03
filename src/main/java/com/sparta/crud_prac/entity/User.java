package com.sparta.crud_prac.entity;

import com.sparta.crud_prac.dto.user.UserSignupRequestDto;
import com.sparta.crud_prac.exception.customException.CustomException;
import jakarta.persistence.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

//    @Size(min = 4, max = 10)    // 길이 최소 4자이상 10자 이하
//    @PositiveOrZero             // 양수와 0만 가능
//    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    // 정규식 표현 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)
    @Column(nullable = false, unique = true)
    private String username;

//    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    // 정규식 표현 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(UserSignupRequestDto sign, UserRoleEnum role) {
        this.username = sign.getUsername();
        this.password = sign.getPassword();
        this.email = sign.getEmail();
        this.role = role;
    }

    public static void inputIsMatches(String username, String password){
        String usernamePattern = "^[a-z0-9]{4,10}$";
        String passwordPattern = "^(?=.*[~!@#$%^&*()_+-])[a-zA-Z0-9~!@#$%^&*()_+-]{8,15}$";
//        String passwordPattern = "^(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+]){8,15}$";

        /*
            ^: 문자열의 시작을 나타냅니다.
            (?=.*[a-z]): 최소한 1개 이상의 소문자가 반드시 포함되어야 함을 나타냅니다.
            (?=.*[A-Z]): 최소한 1개 이상의 대문자가 반드시 포함되어야 함을 나타냅니다.
            (?=.*\d): 최소한 1개 이상의 숫자가 반드시 포함되어야 함을 나타냅니다.
            (?=.*[~!@#$%^&*()_+]): 최소한 1개 이상의 특수문자가 반드시 포함되어야 함을 나타냅니다.
            [a-zA-Z\d~!@#$%^&*()_+]: 소문자, 대문자, 숫자, 특수문자 중에서 하나가 반복되어야 함을 나타냅니다.
            {8,15}: 최소 8자 이상, 15자 이하로 이루어져야 함을 나타냅니다.
            $: 문자열의 끝을 나타냅니다.
        */


        Pattern pattern = Pattern.compile(usernamePattern);
        Matcher matcher = pattern.matcher(username);
        if(!matcher.matches()){ // 정규식 검사를 해서 통과 못하면
            throw new CustomException(ExceptionEnum.INVALID_INPUT_USERNAME);
        }

        pattern = Pattern.compile(passwordPattern);
        matcher = pattern.matcher(password);
        if(!matcher.matches()){
            throw new CustomException(ExceptionEnum.INVALID_INPUT_PASSWORD);
        }
    }
}
