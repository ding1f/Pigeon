package com.pigeon.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Redis配置参数
 *
 * @author Idenn
 * @date 3/6/2024 2:21 PM
 */
@Data
@Configuration
@ConditionalOnProperty(value = "pigeon.middleware.redis.enabled", matchIfMissing = false)
@ConfigurationProperties(prefix = "pigeon.middleware.redis", ignoreUnknownFields = true)
public class RedisPropertiesConfig {
    /**
     * 连接池的最大数据库连接数。设为0表示无限制
     */
    private int maxTotal = 8;
    /**
     * 连接池中的最大空闲连接 默认 8
     */
    private int maxIdle = 8;
    /**
     * 连接池中的最小空闲连接 默认 0
     */
    private int minIdle = 0;
    /**
     * 地址
     */
    // @Value(value = "${pigeon.middleware.redis.host}")
    private String host;
    /**
     * 端口
     */
    // @Value(value = "${pigeon.middleware.redis.port}")
    private int port;
    /**
     * 几号数据库
     */
    // @Value(value = "${pigeon.middleware.redis.database}")
    private int database;
    /**
     * 密码
     */
    // @Value(value = "${pigeon.middleware.redis.password}")
    private String password;
    /**
     * 连接超时时间
     */
    private int timeout = 60;
    /**
     * 断开超时时间
     */
    private int shutdownTimeout = 100;
    /**
     * 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
     */
    private int maxWait = -1;
    /**
     * 空闲对象逐出器线程的运行间隔时间。当为正值时，空闲对象逐出器线程启动，否则不执行空闲对象逐出
     */
    private int timeBetweenEvictionRuns;
    /**
     * 是否开启
     */
    private boolean enabled;
}
