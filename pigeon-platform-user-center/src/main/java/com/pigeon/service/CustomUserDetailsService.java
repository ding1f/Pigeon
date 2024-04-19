package com.pigeon.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigeon.dao.user.UserMapper;
import com.pigeon.entity.pojo.JwtUserPOJO;
import com.pigeon.entity.dto.UserDTO;
import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.entity.user.po.UserPO;
import com.pigeon.enums.ResultCode;
import com.pigeon.exception.BaseException;
import com.pigeon.service.user.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Security的UserDetailService实现类
 *
 * @author Idenn
 * @date 3/18/2024 9:50 PM
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private TokenStore tokenStore;

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据账号查询用户信息
        // UserPO userPO = userMapper.getUserByUsername(username);
        // 通过用户名、邮箱、手机号+密码进行登录
        UserPO userPO = userMapper.getUserByUsernameOrEmailOrTelephone(username);
        if (userPO == null) {
            // 这里其实是【用户名不存在】的错误，但是抛出了一个混淆的错误信息
            throw new BaseException(ResultCode.UNAUTHORIZED.getCode(), "用户名不存在或密码错误！");
        }
        // 将userPO转成json，并且目前看来，loadUserByUsername 只用在了登录时校验用户名和密码中，无需权限信息
        String principal = JSON.toJSONString(userPO);
        return User.withUsername(principal).password(userPO.getPassword()).authorities(new ArrayList<>()).build();
    }

    /**
     * 通过用户名获取用户信息，并且对密码进行脱敏处理
     * 和上面的方法的区别为，这里是返回的jwt是作为token，所以需要对密码进行脱敏，而且需要权限信息
     *
     * @param username 登录的用户名
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author Idenn
     * @date 3/21/2024 6:02 PM
     */
    public UserDetails loadUserByUsernameWithoutPassword(String username) throws UsernameNotFoundException, JsonProcessingException {

        // 根据账号查询用户信息
        // UserPO userPO = userMapper.getUserByUsername(username);
        // 通过用户名、邮箱、手机号+密码进行登录
        UserPO userPO = userMapper.getUserByUsernameOrEmailOrTelephone(username);
        if (userPO == null) {
            // 这里其实是【用户名不存在】的错误，但是抛出了一个混淆的错误信息
            throw new BaseException(ResultCode.UNAUTHORIZED.getCode(), "用户名不存在或密码错误！");
        }
        String userObjectId = userPO.getObjectId();
        String realUsername = userPO.getUsername();

        // 缓存userInfo到redis
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userPO, userDTO);
        userInfoService.setUserInfo2Cache(userDTO);

        // 将userPO转成json
        JwtUserPOJO jwtUserPOJO = new JwtUserPOJO();
        jwtUserPOJO.setObjectId(userObjectId);
        jwtUserPOJO.setUsername(realUsername);

        String principal = JSON.toJSONString(jwtUserPOJO);

        // sso 单点登录
        if (securityContextPOJO.isSsoEnabled()) {
            Collection<OAuth2AccessToken> tokensByClientIdAndUserName = tokenStore.findTokensByClientIdAndUserName(securityContextPOJO.getClientId(), principal);
            for (OAuth2AccessToken token : tokensByClientIdAndUserName) {
                tokenStore.removeAccessToken(token);
            }
        }

        return User.withUsername(principal).password("").authorities(new ArrayList<>()).build();

    }
}
