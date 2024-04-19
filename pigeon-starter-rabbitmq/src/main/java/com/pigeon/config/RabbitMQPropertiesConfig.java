package com.pigeon.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * rabbitmq的参数声明
 *
 * @author Idenn
 * @date 4/2/2024 9:09 AM
 */
@Data
@Component
@Configuration
@ConditionalOnProperty(value = "pigeon.middleware.rabbitmq.enabled", matchIfMissing = false)
@ConfigurationProperties(prefix = "pigeon.middleware.rabbitmq", ignoreUnknownFields = true)
public class RabbitMQPropertiesConfig {

    /**
     * 地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * vhost虚拟机
     */
    private String vHost;

    /**
     * 并发消费者
     */
    private int concurrentConsumers;

    /**
     * 最大并发消费者
     */
    private int maxConcurrentConsumers;

    /**
     * 循环尝试发送时间
     */
    private String resendFreq;

    /**
     * 最大重试次数
     */
    private String maxResendCount;

    /*
     * 是否启用
     */
    private boolean enabled;

    /*
     * key存储到redis中的前缀，保持全局唯一
     */
    private String redisKeyPrefix;

    /*
     * key的暂存时间，默认为7天
     */
    private long accessTokenValiditySeconds = 7 * 24 * 60 * 60;

}
