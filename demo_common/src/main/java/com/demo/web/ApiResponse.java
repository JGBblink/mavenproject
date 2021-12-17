package com.demo.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private T data;
    private Integer code;
    private Date timestamp;

    public static <T> ApiResponse success(T t) {
        return new ApiResponse("ok", t, 200, new Date());
    }

    public static <T> ApiResponse success(String status, T t) {
        return new ApiResponse(status, t, 200, new Date());
    }

    public static <T> ApiResponse success(String status, T t, Integer code) {
        return new ApiResponse(status, t, code, new Date());
    }

    public static <T> ApiResponse failure(String status) {
        return new ApiResponse(status, null, 500, new Date());
    }

    public static <T> ApiResponse failure(String status, Integer code) {
        return new ApiResponse(status, null, code, new Date());
    }

    public static <T> ApiResponse failure(Throwable e) {
        return new ApiResponse(e.getMessage(), null, 500, new Date());
    }

    public static <T> ApiResponse failure(String status, Throwable e) {
        return new ApiResponse(status, null, 500, new Date());
    }
}
