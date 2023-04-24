package com.sparta.crud_prac.exception.customException;

import com.sparta.crud_prac.entity.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ExceptionEnum errorCode;
}
