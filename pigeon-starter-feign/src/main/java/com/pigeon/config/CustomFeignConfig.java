package com.pigeon.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon.context.UserHolder;
import com.pigeon.entity.pojo.UserPOJO;
import com.pigeon.exception.BaseException;
import com.pigeon.utils.FeignBase64Util;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign配置文件
 *
 * @author Idenn
 * @date 3/30/2024 5:34 AM
 */
@Configuration
public class CustomFeignConfig implements RequestInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private FeignBase64Util feignBase64Util;

    /**
     * feign拦截器
     *
     * @return feign.RequestInterceptor
     * @author Idenn
     * @date 3/30/2024 7:23 AM
     */
    @Bean
    public RequestInterceptor headerInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (null != headerNames) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        // 不能把当前请求得content-length传递到下游服务提供方
                        // 请求可能一直返回不了。或者请求响应被截断
                        if (!name.equalsIgnoreCase("content-length")) {
                            requestTemplate.header(name, values);
                        }
                    }
                }
                requestTemplate.header("request-type", "feign");
                UserPOJO user = UserHolder.getUser();
                if (user != null) {
                    // 将用户信息添加到请求头中，例如用户ID或Token
                    try {
                        // 解决中文乱码问题
                        requestTemplate.header("user-info", feignBase64Util.base64Encoder(objectMapper.writeValueAsString(user)));
                    } catch (JsonProcessingException e) {
                        throw new BaseException("Json序列化失败，请检查用户信息！");
                    }
                } else {
                    requestTemplate.header("feign-times", "1");
                }
            } else {
                // 非web请求feign调用手动设置内部系统来源不校验token但需要校验来源@InnerApi
                requestTemplate.header("request-type", "system");
            }

        };
    }

    @Override
    public void apply(RequestTemplate template) {
        String xid = RootContext.getXID();
        if(StringUtils.hasText(xid)){
            template.header(RootContext.KEY_XID, xid);
        }
    }
}
