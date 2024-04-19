package com.pigeon.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Service层接口声明
 *
 * @author Idenn
 * @date 3/6/2024 2:46 PM
 */
public interface RedisService {

    /**
     * @description 无过期时间设置值
     *
     * @param key
     * @param value
     * @return void
     * @author Idenn
     * @date 3/6/2024 5:51 PM
     */
    void set(String key, Object value);

    /**
     * @description 带过期时间默认秒设置值
     *
     * @param key
     * @param value
     * @param timeout
     * @return void
     * @author Idenn
     * @date 3/6/2024 5:51 PM
     */
    void set(String key, Object value, long timeout);

    /**
     * @description 带过期时间设置值
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return void
     * @author Idenn
     * @date 3/6/2024 5:52 PM
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    boolean delete(String key);

    long deleteByPattern(String pattern);

    Set<String> keysByPattern(String pattern);

    String getString(String key);

    <T> List<T> getList(String key, Class<T> clazz);

    <T> T getPojo(String key, Class<T> clazz);

    boolean expire(String key, Long outTime);

    void setHashKV(String hashKey, String key, Object value);

    boolean hasKey(String key);

    Long getExpire(String key);
}
