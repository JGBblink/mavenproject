package com.demo.main.demo;

import com.demo.main.config.interceptor.RequestContext;
import com.demo.main.domain.User;
import com.demo.main.service.DemoService;
import com.demo.web.ApiResponse;
import java.io.FileInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/test")
    public ApiResponse<User> test(HttpServletRequest request, int index) {
        System.out.println(RequestContext.getRequestAttr("user", String.class));
        return ApiResponse.success(demoService.listUsers("test"));
    }

    @GetMapping("/test2")
    public void test2(HttpServletResponse response) throws Exception {
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buff = new byte[1024];
        int size;
        FileInputStream inputStream = new FileInputStream("/Users/cjin/Desktop/截屏2021-12-16 下午3.26.36-zh.png");
        while ((size = inputStream.read(buff)) != -1) {
            outputStream.write(buff);
        }
        outputStream.flush();
        response.setContentType("application/octet-stream");
    }
}
