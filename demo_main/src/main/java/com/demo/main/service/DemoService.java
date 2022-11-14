package com.demo.main.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.main.domain.User;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;

public interface DemoService {

    @Cacheable(value = "testCacheable", key = "#p0", cacheManager = "10m" ,unless = "#result==null")
    List<User> listUsers(String appName);
}
