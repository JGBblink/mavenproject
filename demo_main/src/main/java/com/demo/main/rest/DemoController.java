package com.demo.main.rest;

import com.demo.main.domain.User;
import com.demo.main.service.DemoService;
import com.demo.web.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @PreAuthorize("hasAuthority('USERX')")
    @GetMapping("/test")
    public ApiResponse<User> test() {
        return ApiResponse.success(demoService.listUsers("test"));
    }
}
