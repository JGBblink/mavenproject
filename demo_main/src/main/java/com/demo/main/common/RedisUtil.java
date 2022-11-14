package com.demo.main.common;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;

    public boolean hasKey(String key) {
        checkKey(key);
        return BooleanUtil.isTrue(redisTemplate.hasKey(key));
    }

    public boolean hasNotKey(String key) {
        checkKey(key);
        return BooleanUtil.isFalse(redisTemplate.hasKey(key));
    }

    // ---------- set method ----------
    public boolean set(String key, Object data) {
        return setToObject(key, data);
    }

    public boolean setToJson(String key, Object data) {
        return setToJson(key, JSONUtil.toJsonStr(data));
    }

    // ---------- get method ----------
    public <T> T getAndSet(String key, T data) {
        checkKey(key);
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        return (T) opsForValue.getAndSet(key, data);
    }

    public <T> T get(String key, Class<T> type) {
        return getForObject(key, type);
    }

    public <T> T getFormJson(String key, Class<T> type) {
        return getForJson(key, type);
    }

    public <T> List<T> getFormList(String key, Class<T> type) {
        return getForJsonArr(key, type);
    }

    // ---------- private method ----------

    private boolean setToObject(String key, Object data) {
        checkKey(key);
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key, data);
        return true;
    }

    private <T> T getForObject(String key, Class<T> type) {
        checkKey(key);
        ValueOperations opsForValue = redisTemplate.opsForValue();
        Object value = opsForValue.get(key);
        if (Objects.isNull(value)) {
            return null;
        }

        return (T) value;
    }

    private boolean setToJson(String key, String data) {
        checkKey(key);
        ValueOperations<String, String> opsForValue = strRedisTemplate.opsForValue();
        opsForValue.set(key, data);
        return true;
    }

    private <T> T getForJson(String key, Class<T> type) {
        checkKey(key);
        ValueOperations<String, String> opsForValue = strRedisTemplate.opsForValue();
        String o = opsForValue.get(key);
        if (Objects.nonNull(o)) {
            return JSONUtil.toBean(o, type);
        }
        return null;
    }

    private <T> List<T> getForJsonArr(String key, Class<T> type) {
        checkKey(key);
        ValueOperations<String, String> opsForValue = strRedisTemplate.opsForValue();
        String o = opsForValue.get(key);
        if (Objects.nonNull(o)) {
            return JSONUtil.toList(JSONUtil.parseArray(o), type);
        }
        return null;
    }

    private void checkKey(String key) {
        if (Objects.isNull(key)) {
            throw new RuntimeException("key not be null");
        }
    }

}
