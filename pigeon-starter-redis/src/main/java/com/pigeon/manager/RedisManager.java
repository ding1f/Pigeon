package com.pigeon.manager;

import com.alibaba.fastjson.JSON;
import com.pigeon.service.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Redis管理
 *
 * @author Idenn
 * @date 3/6/2024 2:38 PM
 */
@Component
public class RedisManager {

    @Resource
    RedisService redisService;

    @Resource
    RedisLockManager redisLockManager;

    /**
     * 无过期时间设置值
     *
     * @param key   key
     * @param value 值
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:57 PM
     */
    public boolean set(String key, Object value) {
        return set(key, value, null);
    }

    /**
     * 无过期时间设置值-实体
     *
     * @param key   key
     * @param value 值
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:58 PM
     */
    public <T> boolean setPojo(String key, T value) {
        return set(key, JSON.toJSONString(value), null);
    }


    /**
     * 无过期时间设置值 加写锁
     *
     * @param key     key
     * @param value   值
     * @param lockKey 锁key
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:58 PM
     */
    public boolean set(String key, Object value, String lockKey) {
        if (lockKey == null) {
            redisService.set(key, value);
            return true;
        } else {
            return redisLockManager.writeLock(lockKey, () -> redisService.set(key, value));
        }
    }

    /**
     * @description 带过期时间默认秒设置值
     *
     * @param key     key
     * @param value   值
     * @param timeout 超时
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 3:06 PM
     */
    public boolean setExpire(String key, Object value, Long timeout) {
        return setExpire(key, value, timeout, null);
    }

    /**
     * @description 带过期时间默认秒设置值
     *
     * @param key     key
     * @param value   值
     * @param timeout 超时
     * @param lockKey 锁key
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 3:07 PM
     */
    public boolean setExpire(String key, Object value, Long timeout, String lockKey) {
        if (lockKey == null) {
            redisService.set(key, value, timeout);
            return true;
        } else {
            return redisLockManager.writeLock(lockKey, () -> redisService.set(key, value, timeout));
        }
    }

    /**
     * @description 获取字符串
     *
     * @param key key
     * @return java.lang.String
     * @author Idenn
     * @date 3/6/2024 3:07 PM
     */
    public String getString(String key) {
        return redisService.getString(key);
    }

    /**
     * @description 获取实体
     *
     * @param key   key
     * @param clazz 实体类型
     * @return T
     * @author Idenn
     * @date 3/6/2024 3:07 PM
     */
    public <T> T getPojo(String key, Class<T> clazz) {
        return redisService.getPojo(key, clazz);
    }

    /**
     * @description 获取集合
     * 
     * @param key   key
     * @param clazz 集合类型
     * @return java.util.List<T>
     * @author Idenn
     * @date 3/6/2024 3:08 PM
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        return redisService.getList(key, clazz);
    }

    /**
     * @description 获取集合
     *
     * @param pattern 通配
     * @param clazz   集合类型
     * @return java.util.List<T>
     * @author Idenn
     * @date 3/6/2024 3:08 PM
     */
    public <T> List<T> getListPojo(String pattern, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        Set<String> keys = redisService.keysByPattern(pattern);
        for (String key : keys) {
            result.add(redisService.getPojo(key, clazz));
        }
        return result;
    }

    /**
     * @description 按照key进行删除
     *
     * @param key key
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 3:09 PM
     */
    public boolean delete(String key) {
        return redisService.delete(key);
    }

    /**
     * @description 按照key进行通配删除
     *
     * @param pattern 通配
     * @return long
     * @author Idenn
     * @date 3/6/2024 3:09 PM
     */
    public long deleteByPattern(String pattern) {
        return redisService.deleteByPattern(pattern);
    }

    /**
     * @description 带过期时间默认秒设置值
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   值
     * @return void
     * @author Idenn
     * @date 3/6/2024 3:09 PM
     */
    public void setHashKV(String key, String hashKey, Object value) {
        redisService.setHashKV(key, hashKey, value);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return boolean
     * @author Idenn
     * @date 3/19/2024 3:22 PM
     */
    public boolean hasKey(String key) {
        return redisService.hasKey(key);
    }

    /**
     * 获取key的过期时间
     *
     * @param key
     * @return java.lang.Long
     * @author Idenn
     * @date 3/29/2024 5:34 PM
     */
    public Long getExpire(String key) {
        return redisService.getExpire(key);
    }

    /**
     * 刷新TTL
     *
     * @param key
     * @param timeout
     * @return boolean
     * @author Idenn
     * @date 4/1/2024 7:46 PM
     */
    public boolean expire(String key, Long timeout) {
        return redisService.expire(key, timeout);
    }

}
