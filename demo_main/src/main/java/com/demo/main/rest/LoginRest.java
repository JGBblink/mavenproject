package com.demo.main.rest;

import com.demo.main.domain.User;
import com.demo.main.service.DemoService;
import com.demo.web.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginRest {

    @Autowired
    private DemoService demoService;

    @PostMapping("")
    public ApiResponse<User> login() {
        return ApiResponse.success(demoService.listUsers("test"));
    }

    @PostMapping("/out")
    public ApiResponse<User> loginOut() {
        return ApiResponse.success(demoService.listUsers("test"));
    }

}
