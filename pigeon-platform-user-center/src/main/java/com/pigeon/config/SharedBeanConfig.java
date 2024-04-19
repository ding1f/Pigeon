package com.pigeon.config;

import com.pigeon.entity.pojo.SecurityContextPOJO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/**
 * 解决循环依赖
 *
 * @author Idenn
 * @date 4/1/2024 4:31 PM
 */
@Configuration
public class SharedBeanConfig {

    /*
     * 认证模块上下文POJO
     */
    @Resource
    private SecurityContextPOJO securityContextPOJO;

    /*
     * redis连接工厂，这里使用的是自定义的
     */
    @Resource
    private JedisConnectionFactory jedisConnectionFactory;

    /**
     * token持久化配置
     */
    @Bean
    public TokenStore tokenStore() {
        // 使用redis存储token
        RedisTokenStore redisTokenStore = new RedisTokenStore(jedisConnectionFactory);
        // 设置redis token存储中的前缀
        redisTokenStore.setPrefix(securityContextPOJO.getSetTokenPrefix());
        return redisTokenStore;
    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
