package com.pigeon.entity.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SecurityPOJO类，所有的属性全部在nacos中配置
 *
 *
 * @author Idenn
 * @date 3/20/2024 8:13 PM
 */
@Data
@Component
@ConfigurationProperties("pigeon.middleware.security")
public class SecurityContextPOJO {

    /*
     * 白名单
     */
    private String[] whiteList;

    /*
     * 客户端ID，横向扩展gateway时记得设置为不同的id
     */
    private String clientId;

    /*
     * 用户中心使用的密码，保持全局唯一
     */
    private String clientSecret;

    /*
     * token有效时间，默认为3小时
     */
    private long accessTokenValiditySeconds = 3 * 60 * 60;

    /*
     * refresh token的有效时间，默认为3天
     */
    private long refreshTokenValiditySeconds = 3 * 24 * 60 * 60;

    /*
     * 用户信息作为缓存的TTL，默认为3天
     */
    private long userInfoValiditySeconds = 3 * 24 * 60 * 60;

    /*
     * 签名验证器，保持全局唯一
     */
    private String signingKey;

    /*
     * 存储在redis中的token的前缀，保持全局唯一
     */
    private String setTokenPrefix;

    /*
     * 获取时在redis中的token的前缀，保持全局唯一
     */
    private String getTokenPrefix;

    /*
     * 登出的路径
     */
    private String logoutPath = "/account/logout";

    /*
     * 存储用户信息的前缀，保持全局唯一
     */
    private String userInfoPrefix;

    /*
     * 获取权限的API地址，用来校验请求是否来自内部系统，保持全局唯一
     */
    private String fetchPermissionPath;

    /*
     * 是否开启单点登录，默认关闭
     */
    private boolean ssoEnabled = false;

}
