package com.demo.main.config.auth;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.demo.main.base.jwt.JwtUtilRSA;
import com.demo.main.domain.Role;
import com.demo.main.domain.User;
import com.demo.web.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class AuthVerifyFilter extends BasicAuthenticationFilter {

    public AuthVerifyFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        boolean authFailure = true;
        String token = request.getHeader("Authorization");

        if (StrUtil.isNotBlank(token)) {
            try {
                // 解析token，从token中获取role
                Jwt jwt = JwtUtilRSA.parseToken(token);
                Claims claims = (Claims) jwt.getBody();
                List<Role> roles = JSONUtil.toList(JSONUtil.parseArray(claims.getSubject()), Role.class);
                // 设置上下文认证对象
                Authentication authResult = new UsernamePasswordAuthenticationToken
                        (null, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authResult);
                authFailure = false;
                chain.doFilter(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 认证失败
        if (authFailure) {
            ApiResponse.failure(response,"认证失败", HttpStatus.HTTP_UNAUTHORIZED);
        }
    }
}

