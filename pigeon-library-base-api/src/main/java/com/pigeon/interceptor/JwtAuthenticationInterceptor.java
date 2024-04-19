package com.pigeon.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon.context.UserHolder;
import com.pigeon.entity.dto.UserDTO;
import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.entity.pojo.UserPOJO;
import com.pigeon.enums.ResultCode;
import com.pigeon.exception.BaseException;
import com.pigeon.feign.UserCenterClient;
import com.pigeon.utils.FeignBase64Util;
import com.pigeon.utils.JwtTokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 解析jwt放入UserHolder
 *
 * @author Idenn
 * @date 3/25/2024 3:23 PM
 */
public class JwtAuthenticationInterceptor implements HandlerInterceptor, Ordered {

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

    @Resource
    private UserCenterClient userCenterClient;

    @Resource
    private FeignBase64Util feignBase64Util;

    /**
     * 调用时间：Controller方法处理之前
     * 执行顺序：链式Intercepter情况下，Intercepter按照声明的顺序一个接一个执行
     * 若返回false，则中断执行，注意：不会进入afterCompletion
     *
     * @param request
     * @param response
     * @param handler
     * @return boolean
     * @author Idenn
     * @date 3/25/2024 3:30 PM
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // 检查是否在白名单中
        if (Arrays.asList(securityContextPOJO.getWhiteList()).contains(requestURI)) {
            return true;
        }

        // 判断请求是否来自feign，如果是则从请求头中获取用户信息
        String header = request.getHeader("request-type");
        if (!StringUtils.isEmpty(header) && "feign".equals(header)) {
            String userInfoStr = request.getHeader("user-info");
            // 通过feign调用时，第一次是用来获取UserHolder的，直接放行
            if (StringUtils.isEmpty(userInfoStr) && "1".equals(request.getHeader("feign-times"))) {
                return true;
            }
            UserPOJO userPOJO = objectMapper.readValue(feignBase64Util.base64Decoder(userInfoStr), UserPOJO.class);
            UserHolder.setUser(userPOJO);
            return true;
        } else if (!StringUtils.isEmpty(header) && "system".equals(header) && securityContextPOJO.getFetchPermissionPath().equals(request.getRequestURI())) {
            // 只对来自系统的查询权限列表操作进行放行
            return true;
        }

        // 从请求头中获取Token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉Bearer前缀
            // 解析Token，获取用户信息
            String userInfo = jwtTokenUtil.getUserInfoFrom(token);
            UserPOJO userPOJO = objectMapper.readValue(userInfo, UserPOJO.class);
            UserDTO userDTO = userCenterClient.fetchUserInfoFromCache(userPOJO.getObjectId()).getData();
            BeanUtils.copyProperties(userDTO, userPOJO);
            userPOJO.setToken(token);

            // 将用户信息存入UserHolder
            UserHolder.setUser(userPOJO);
            return true; // 放行请求
        }

        // 如果没有Token或Token无效，则根据实际情况处理，例如返回401状态码
        throw new BaseException(ResultCode.UNAUTHORIZED.getCode(), "用户信息获取失败，请检查token的格式是否正确！");
    }

    /**
     * 调用前提：preHandle返回true
     * 调用时间：DispatcherServlet进行视图的渲染之后
     * 清空UserHolder信息防止内存泄露
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return void
     * @author Idenn
     * @date 3/25/2024 3:29 PM
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.clear();
    }
}
