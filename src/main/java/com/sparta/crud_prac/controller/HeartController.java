package com.sparta.crud_prac.controller;


import com.sparta.crud_prac.dto.heart.HeartRequsetDto;
import com.sparta.crud_prac.dto.heart.HeartResponseDto;
import com.sparta.crud_prac.security.UserDetailsImpl;
import com.sparta.crud_prac.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/heart")
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/hearts/posts/{id}")
    public HeartResponseDto heartPost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return heartService.heartPost(id, userDetails);
    }

    @PostMapping("/hearts/comments/{id}")
    public HeartResponseDto heartComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return heartService.heartComment(id, userDetails);
    }

}
