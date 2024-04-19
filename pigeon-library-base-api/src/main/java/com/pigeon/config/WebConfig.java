package com.pigeon.config;

import com.pigeon.interceptor.JwtAuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author Idenn
 * @date 3/25/2024 3:46 PM
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public JwtAuthenticationInterceptor jwtAuthenticationInterceptor() {
        return new JwtAuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor())
                .addPathPatterns("/**") // 对所有路径应用拦截器
                .excludePathPatterns("/error"); // 除了登录和错误路径
    }
}
