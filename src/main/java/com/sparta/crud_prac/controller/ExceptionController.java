package com.sparta.crud_prac.controller;

import com.sparta.crud_prac.dto.exceptions.ErrorResponseDto;
import com.sparta.crud_prac.entity.ExceptionEnum;
import com.sparta.crud_prac.exception.customException.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(final CustomException e){
        return ResponseEntity.status(e.getErrorCode().getStatus().value())
                .body(new ErrorResponseDto(e.getErrorCode()));
    }
}
