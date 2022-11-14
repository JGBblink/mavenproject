package com.cn.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cn.main.dao.mapper")
public class WebMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMainApplication.class, args);
    }
}
