package com.demo.main.config.auth;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.demo.main.base.jwt.JwtUtilRSA;
import com.demo.main.domain.Role;
import com.demo.main.domain.User;
import com.demo.web.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class LoginVerifyFilter extends UsernamePasswordAuthenticationFilter {

    public LoginVerifyFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            return super.attemptAuthentication(request, response);
        } catch (Exception e) {
            ApiResponse.failure(response, "账号或密码错误", HttpStatus.HTTP_BAD_REQUEST);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            String token = JwtUtilRSA.generateToken(authResult.getAuthorities(), 30);
            response.addHeader("Authorization", token);    //将Token信息返回给用户
        } catch (Exception e) {
            e.printStackTrace();
        }

        //登录成功时，返回json格式进行提示
        ApiResponse.success(response, "登陆成功");
    }
}
