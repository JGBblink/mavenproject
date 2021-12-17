package com.demo.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.main.domain.User;
import com.demo.main.mapper.UserMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements com.demo.main.service.DemoService {

    @Value("${spring.application.name:none}")
    private String appName;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> listUsers(String appName) {
        return userMapper.selectList(null);
    }
}
