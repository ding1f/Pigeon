package com.pigeon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.enums.ResultCode;
import com.pigeon.exception.BaseException;
import com.pigeon.manager.RedisManager;
import com.pigeon.response.CommonResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出时废弃JWT
 *
 * @author Idenn
 * @date 4/1/2024 4:17 PM
 */
@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Resource
    private TokenStore tokenStore;

    @Resource
    private RedisManager redisManager;

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        // 从请求中获取令牌
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                String hasKeyToken = securityContextPOJO.getGetTokenPrefix() + token;
                if (!redisManager.hasKey(hasKeyToken)) {
                    throw new BaseException("登出用户token校验失败！");
                }

                // 从TokenStore中移除令牌，使其失效
                tokenStore.removeAccessToken(tokenStore.readAccessToken(token));

                objectMapper.writeValue(response.getOutputStream(), new CommonResponse<>(ResultCode.SUCCESS.getCode(), "登出成功！", null));
            } else {
                throw new BaseException("token获取失败！");
            }
        } catch (BaseException e) {
            // 处理异常情况，返回错误信息
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getOutputStream(), new CommonResponse<>(ResultCode.BAD_REQUEST.getCode(), e.getMessage(), null));
        }

    }
}