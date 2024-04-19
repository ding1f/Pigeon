package com.pigeon.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis的通用配置
 *
 * @author Idenn
 * @date 3/5/2024 9:47 PM
 */
@Configuration
@ConditionalOnProperty(value = "pigeon.middleware.redis.enabled", matchIfMissing = false)
public class RedisConfig {

    /**
     * 构建连接连接工厂
     * @param propertiesConfig 配置文件
     * @return org.springframework.data.redis.connection.jedis.JedisConnectionFactory
     * @author Idenn
     * @date 3/6/2024 2:20 PM
     */
    @Bean
    public JedisConnectionFactory redisConnectionFactory(RedisPropertiesConfig propertiesConfig) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(propertiesConfig.getMaxTotal());
        jedisPoolConfig.setMaxIdle(propertiesConfig.getMaxIdle());
        jedisPoolConfig.setMinIdle(propertiesConfig.getMinIdle());
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(propertiesConfig.getHost());
        redisStandaloneConfiguration.setPort(propertiesConfig.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(propertiesConfig.getPassword()));
        redisStandaloneConfiguration.setDatabase(propertiesConfig.getDatabase());
        JedisClientConfiguration.JedisClientConfigurationBuilder configurationBuilder = JedisClientConfiguration.builder();
        JedisClientConfiguration jedisClientConfiguration = configurationBuilder.usePooling().poolConfig(jedisPoolConfig).build();
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    /**
     * 构建Redis连接工具
     * @param redisConnectionFactory 连接工厂
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.Object,java.lang.Object>
     * @author Idenn
     * @date 3/6/2024 2:20 PM
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        return redisTemplate;
    }
}
