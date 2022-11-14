package com.demo.main.demo;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.demo.main.common.RedisUtil;
import com.demo.main.domain.User;
import com.demo.main.service.DemoService;
import com.demo.web.ApiResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class RedisController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DemoService demoService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/redis/mainTest")
    public ApiResponse<?> mainTest(@RequestParam String id) {
        String key = "JGB:test:" + id;
        ZSetOperations<String, Object> zSet = redisTemplate.opsForZSet();
        Set<Object> range = zSet.range("seasons~keys", 0, 100);
        Object seasons_2150345398432_false = redisTemplate.opsForValue().get("SEASONS_2150345398432_false");

        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        List s = new ArrayList();
        s.add(100);
        operations.set("SEASONS:syn:season:2150345398432",s);

        return ApiResponse.success("");
    }

    @GetMapping("/redis")
    public ApiResponse<List> redisDemo(@RequestParam String id) {
        String key = "JGB:test:" + id;
        if (redisUtil.hasNotKey(key)) {
            List<User> users = demoService.listUsers(id);
            redisUtil.set(key, users);
        }

        String andSet = redisUtil.getAndSet("GS:" + key, "SG" + id);
        System.out.println(andSet);

        List<User> users = redisUtil.get(key, List.class);
        return ApiResponse.success(users);
    }

    @GetMapping("/redis/json")
    public ApiResponse<?> redisDemoJson(@RequestParam String id) {
        String key = "JGB:test:" + id;
        if (redisUtil.hasNotKey(key)) {
            List<User> users = demoService.listUsers("test");
            redisUtil.setToJson(key,users);
        }

        List<User> users = redisUtil.getFormList(key, User.class);
        return ApiResponse.success(users);
    }

}
