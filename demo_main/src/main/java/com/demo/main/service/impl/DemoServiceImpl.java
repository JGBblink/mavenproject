package com.demo.main.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements com.demo.main.service.DemoService {

    @Value("${spring.application.name:none}")
    private String appName;

    @Override
    public String testMethod(String appName) {
        return appName + " " + this.appName;
    }
}
