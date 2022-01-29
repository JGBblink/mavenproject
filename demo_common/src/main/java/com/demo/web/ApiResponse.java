package com.demo.web;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
    private Integer code;
    private Date timestamp;

    public static <T> ApiResponse success(T t) {
        return new ApiResponse("ok", t, 200, new Date());
    }

    public static <T> ApiResponse success(String message, T t) {
        return new ApiResponse(message, t, 200, new Date());
    }

    public static <T> ApiResponse success(String message, T t, Integer code) {
        return new ApiResponse(message, t, code, new Date());
    }

    public static <T> ApiResponse failure(String message) {
        return new ApiResponse(message, null, 500, new Date());
    }

    public static <T> ApiResponse failure(String message, Integer code) {
        return new ApiResponse(message, null, code, new Date());
    }

    public static <T> ApiResponse failure(Throwable e) {
        return new ApiResponse(e.getMessage(), null, 500, new Date());
    }

    public static <T> ApiResponse failure(String message, Throwable e) {
        return new ApiResponse(message, null, 500, new Date());
    }

    public static void failure(HttpServletResponse response, String message, Integer status) {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(Objects.isNull(status) ? HttpStatus.HTTP_INTERNAL_ERROR : status);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JSONUtil.toJsonStr(ApiResponse.failure(message, response.getStatus())));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (Objects.nonNull(out)) {
                out.flush();
                out.close();
            }
        }
    }

    public static void success(HttpServletResponse response, String message) {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.HTTP_OK);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(JSONUtil.toJsonStr(ApiResponse.success(message, response.getStatus())));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (Objects.nonNull(out)) {
                out.flush();
                out.close();
            }
        }
    }
}
