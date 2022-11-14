package com.demo.main.common;

import cn.hutool.http.HttpStatus;
import com.demo.web.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebGlobalException {

    @ExceptionHandler(value = Exception.class)
    public ApiResponse errorHandler(Exception e) {
        return ApiResponse.failure(e);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ApiResponse tokenExpiredHandler(Exception e) {
        return ApiResponse.failure("token已过期", HttpStatus.HTTP_UNAUTHORIZED);
    }

}
