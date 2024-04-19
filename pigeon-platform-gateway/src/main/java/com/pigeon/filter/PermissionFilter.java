package com.pigeon.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon.entity.pojo.JwtUserPOJO;
import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.enums.ResultCode;
import com.pigeon.exception.BaseException;
import com.pigeon.feign.UserCenterClient;
import com.pigeon.manager.RedisManager;
import com.pigeon.utils.JwtTokenUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 权限过滤器
 *
 * @author Idenn
 * @date 3/30/2024 5:34 AM
 */
@Component
public class PermissionFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private RedisManager redisManager;

    @Resource
    private UserCenterClient userCenterClient;

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getPath().pathWithinApplication().value();

        // 检查 URI 是否在白名单中
        for (String ignoredPath : securityContextPOJO.getWhiteList()) {
            if (antPathMatcher.match(ignoredPath, uri)) {
                return chain.filter(exchange); // 白名单中的路径直接放行
            }
        }

        // 从请求头中获取 Authorization 字段
        String authorizationHeader = request.getHeaders().getFirst("Authorization");

        // 检查 Authorization 字段是否存在且以 "Bearer " 开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // 截取 "Bearer " 之后的部分作为 JWT token

            boolean hasTokenKey = redisManager.hasKey(securityContextPOJO.getGetTokenPrefix() + token);
            if (!hasTokenKey) {
                // 如果key不存在，则证明token失效
                return Mono.error(new BaseException(ResultCode.UNAUTHORIZED.getCode(), "登录信息已失效，请重新登录！"));
            }
            String userInfo = jwtTokenUtil.getUserInfoFrom(token);
            String userObjectId;
            JwtUserPOJO userPOJO;
            try {
                userPOJO = objectMapper.readValue(userInfo, JwtUserPOJO.class);
            } catch (JsonProcessingException e) {
                return Mono.error(new BaseException("Json解析失败，请检查token！"));
            }
            userObjectId = userPOJO.getObjectId();

            // 尝试从缓存中获取权限信息
            List<String> authorities;
            String userInfoKey = securityContextPOJO.getUserInfoPrefix() + userObjectId;
            String userInfoStr = redisManager.getString(userInfoKey);
            Map<String, Object> retrievedMap;
            if (StringUtils.isEmpty(userInfoStr)) {
                // 从用户中心获取中获取
                // authorities = userCenterClient.fetchPermissionsFromCache(userObjectId).getData();
                Future<List<String>> future = executorService.submit(() -> userCenterClient.fetchPermissionsFromCache(userObjectId).getData());
                try {
                    authorities = future.get();
                } catch (ExecutionException e) {
                    return Mono.error(new BaseException("获取权限失败，请联系管理员！"));
                } catch (InterruptedException e) {
                    return Mono.error(new BaseException("异常中断！"));
                }
            } else {
                try {
                    retrievedMap = objectMapper.readValue(userInfoStr, new TypeReference<Map<String, Object>>() {
                    });
                    authorities = (List<String>) retrievedMap.get("permissions");
                } catch (JsonProcessingException e) {
                    return Mono.error(new BaseException("Json解析失败，请检查token！"));
                }
            }

            // 为所有用户手动添加登出权限
            authorities.add(securityContextPOJO.getLogoutPath());

            // 检查 URI 是否在用户的 authorities 中
            boolean hasAuthority = authorities.stream()
                    .anyMatch(authority -> antPathMatcher.match(authority, uri));
            if (hasAuthority) {
                // 如果存在，则刷新token的持续存在时间
                redisManager.expire(securityContextPOJO.getGetTokenPrefix() + token, securityContextPOJO.getAccessTokenValiditySeconds());
                return chain.filter(exchange);
            } else {
                return Mono.error(new BaseException(ResultCode.FORBIDDEN.getCode(), "权限不足！"));
            }
        }

        // 如果请求头中没有合适的 Authorization 字段，拒绝访问
        return Mono.error(new BaseException(ResultCode.UNAUTHORIZED.getCode(), "登录信息校验失败！"));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
