package com.pigeon.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置
 *
 * @author Idenn
 * @date 3/6/2024 2:25 PM
 */
@Configuration
@ConditionalOnProperty(value = "pigeon.middleware.redis.enabled", matchIfMissing = false)
public class RedissonConfig {

    /**
     * Redisson客户端声明
     * @param redisPropertiesConfig redis配置
     * @return org.redisson.api.RedissonClient
     * @author Idenn
     * @date 3/6/2024 2:27 PM
     */
    @Bean
    public RedissonClient redissonClient(RedisPropertiesConfig redisPropertiesConfig) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        String address = "redis://" + redisPropertiesConfig.getHost() + ":" + redisPropertiesConfig.getPort();
        singleServerConfig.setAddress(address);
        singleServerConfig.setPassword(redisPropertiesConfig.getPassword());
        singleServerConfig.setDatabase(redisPropertiesConfig.getDatabase());
        singleServerConfig.setConnectionPoolSize(64);
        singleServerConfig.setTimeout(3000);
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}
