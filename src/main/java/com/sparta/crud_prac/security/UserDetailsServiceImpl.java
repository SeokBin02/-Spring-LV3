package com.sparta.crud_prac.security;

import com.sparta.crud_prac.entity.ExceptionEnum;
import com.sparta.crud_prac.entity.User;
import com.sparta.crud_prac.exception.customException.CustomException;
import com.sparta.crud_prac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ExceptionEnum.USERS_NOT_FOUND)
        );

        return new UserDetailsImpl(user, user.getUsername(), user.getPassword());
    }
}
