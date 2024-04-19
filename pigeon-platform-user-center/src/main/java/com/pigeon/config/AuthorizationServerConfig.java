package com.pigeon.config;

import com.pigeon.entity.pojo.SecurityContextPOJO;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/**
 * oauth认证服务配置类
 *
 * @author Idenn
 * @date 3/17/2024 4:26 PM
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /*
     * 认证模块上下文POJO
     */
    @Resource
    private SecurityContextPOJO securityContextPOJO;

    /**
     * 认证管理器
     */
    private final AuthenticationManager authenticationManager;

    /**
     * 授权码服务
     */
    private final AuthorizationCodeServices authorizationCodeServices;

    /**
     * 密码加密器
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private TokenStore tokenStore;


    /**
     * 客户端详情服务配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 使用本地内存存储
                .inMemory()
                // 客户端id
                .withClient(securityContextPOJO.getClientId())
                // 客户端密码
                .secret(passwordEncoder.encode(securityContextPOJO.getClientSecret()))
                // 该客户端允许授权的类型
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                // 该客户端允许授权的范围
                .scopes("all")
                // false跳转到授权页面，true不跳转，直接发令牌
                .autoApprove(false);
    }

    /**
     * 配置访问令牌端点
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // 认证管理器，支持密码模式
                .authenticationManager(authenticationManager)
                // 授权码服务，授权码是在授权码模式中使用的一种短暂的凭证。
                .authorizationCodeServices(authorizationCodeServices)
                // 令牌管理服务，令牌是在客户端成功授权后，服务器颁发给客户端的一种长期有效的凭证。
                .tokenServices(tokenServices())
                // 设置了访问令牌端点允许的HTTP方法。这里允许使用GET和POST方法访问。
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
     * 配置令牌端点安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // oauth/check_token 公开，/oauth/check_token端点是用来检查令牌的有效性的。
                .checkTokenAccess("permitAll()")
                // oauth/token_key 公开，/oauth/token_key端点是用来获取公开的密钥的，这个密钥可以用来验证JWT类型的令牌。
                .tokenKeyAccess("permitAll()")
                // 允许表单认证，方便某些不能方便地添加Authorization头的客户端。
                .allowFormAuthenticationForClients();
    }

    /**
     * 令牌服务配置
     * Spring在尝试注入AuthorizationServerTokenServices类型的Bean时
     * 找到了两个匹配的Bean：defaultAuthorizationServerTokenServices和tokenServices
     * Spring不知道应该注入哪一个，所以抛出了NoUniqueBeanDefinitionException异常
     * 所以在这个Bean上加了Primary注解
     *
     * @return 令牌服务对象
     */
    @Primary
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        // 启用refresh_token
        tokenServices.setSupportRefreshToken(false);
        // 复用refresh_token
        tokenServices.setReuseRefreshToken(false);
        // 令牌默认有效期2小时
        tokenServices.setAccessTokenValiditySeconds((int) securityContextPOJO.getAccessTokenValiditySeconds());
        // 刷新令牌默认有效期3天
        tokenServices.setRefreshTokenValiditySeconds((int) securityContextPOJO.getRefreshTokenValiditySeconds());
        // 设置JwtAccessTokenConverter，使用jwt作为token，否则则使用UUID
        tokenServices.setTokenEnhancer(accessTokenConverter());
        return tokenServices;
    }

    /**
     * jwt token解析器
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 对称密钥，资源服务器使用该密钥来验证
        converter.setSigningKey(securityContextPOJO.getSigningKey());
        return converter;
    }

}