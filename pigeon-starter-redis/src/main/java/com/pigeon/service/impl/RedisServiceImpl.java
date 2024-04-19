package com.pigeon.service.impl;

import com.pigeon.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisService的实现类
 *
 * @author Idenn
 * @date 3/6/2024 2:46 PM
 */
@Service
public class RedisServiceImpl implements RedisService {

    /**
     * redis操作基础工具
     */
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public boolean delete(String key) {
        if (key == null) {
            return false;
        }
        return redisTemplate.delete(key);
    }

    @Override
    public long deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        assert keys != null;
        return redisTemplate.delete(keys);
    }

    @Override
    public Set<String> keysByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        assert keys != null;
        return keys;
    }

    @Override
    public String getString(String key) {
        String result = "";
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj != null) {
            result = obj.toString();
        }
        return result;
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        List<T> result;
        JSONArray objects = JSONArray.parseArray(getString(key));
        result = objects.toJavaList(clazz);
        return result;
    }

    @Override
    public <T> T getPojo(String key, Class<T> clazz) {
        T result;
        String str = getString(key);
        result = JSONObject.parseObject(str, clazz);
        return result;
    }

    @Override
    public boolean expire(String key, Long outTime) {
        return redisTemplate.expire(key, outTime, TimeUnit.SECONDS);
    }

    @Override
    public void setHashKV(String hashKey, String key, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }


}
