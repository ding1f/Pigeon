package com.pigeon.service.user.impl;

import com.pigeon.context.UserHolder;
import com.pigeon.dao.mybatis.config.AutoFillConfig;
import com.pigeon.dao.role.RoleMapper;
import com.pigeon.dao.user.UserMapper;
import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.entity.pojo.UserPOJO;
import com.pigeon.entity.role.vo.RoleVO;
import com.pigeon.entity.dto.UserDTO;
import com.pigeon.entity.user.po.UserPO;
import com.pigeon.entity.user.po.UserRolesPO;
import com.pigeon.entity.user.po.UserRolesPermissionsMenusPO;
import com.pigeon.entity.user.vo.*;
import com.pigeon.enums.ResultCode;
import com.pigeon.exception.BaseException;
import com.pigeon.manager.RedisManager;
import com.pigeon.service.CustomUserDetailsService;
import com.pigeon.service.impl.BaseServiceImpl;
import com.pigeon.service.user.AccountService;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * 用户模块实现类
 *
 * @author Idenn
 * @date 3/13/2024 4:02 PM
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl<UserPO> implements AccountService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private ClientDetailsService clientDetailsService;

    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Resource
    private RedisManager redisManager;

    @Resource
    private AutoFillConfig autoFillConfig;

    /**
     * 分页查询用户信息
     *
     * @param userQueryVO
     * @return java.util.List<com.pigeon.entity.user.vo.UserResultVO>
     * @author Idenn
     * @date 3/26/2024 9:01 PM
     */
    @Override
    public List<UserResultVO> getAccountByPage(UserQueryVO userQueryVO) {
        return userMapper.getAccountByPage(userQueryVO);
    }

    /**
     * 注册
     *
     * @param userRegisterVO 注册用信息
     * @return java.lang.String
     * @author Idenn
     * @date 3/16/2024 5:55 PM
     */
    @Override
    @Transactional
    public String register(UserRegisterVO userRegisterVO) {
        String msg = "注册成功！";
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(userRegisterVO, userPO);

        // 唯一性校验
        checkUnique(userPO);

        // 对密码进行加密
        userPO.setPassword(passwordEncoder.encode(userRegisterVO.getPassword()));
        if (userMapper.insert(userPO) == 1) {
            return msg;
        } else {
            throw new BaseException("用户注册失败！");
        }
    }

    /**
     * 根据name获取username
     *
     * @param name 用户名
     * @return com.pigeon.entity.dto.UserDTO
     * @author Idenn
     * @date 3/18/2024 10:19 PM
     */
    @Override
    public UserDTO getUserByUsername(String name) {
        UserPO userPO = userMapper.getUserByUsername(name);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userPO, userDTO);

        return userDTO;
    }

    /**
     * 根据用户名登录
     * feat: 根据倒排索引，可以使用邮箱/手机号进行登录
     *
     * @param userLoginVO 登录用VO，传入的username字段也可能是邮箱或者手机号
     * @return java.lang.String
     * @author Idenn
     * @date 3/21/2024 12:50 AM
     */
    @Override
    public String login(UserLoginVO userLoginVO) throws Exception {

        String clientId = securityContextPOJO.getClientId();
        String clientSecret = securityContextPOJO.getClientSecret();
        String username = userLoginVO.getUsername();
        String password = userLoginVO.getPassword();

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null || !passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            // throw new Exception("Invalid client details");
            throw new BaseException("clientId校验失败，请联系管理员！");
        }

        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(), clientId, clientDetails.getScope(), "password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            // 使用包含密码的 UserDetails 对象进行身份验证
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 使用不包含密码的 UserDetails 对象生成令牌
            UserDetails userDetails = customUserDetailsService.loadUserByUsernameWithoutPassword(username);
            Authentication authenticationForToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authenticationForToken);

            OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

            return token.getValue();
        } catch (BadCredentialsException e) {
            // 这里其实是【密码错误】的错误，但是抛出了一个混淆的错误信息
            throw new BaseException(ResultCode.UNAUTHORIZED.getCode(), "用户名不存在或密码错误！");
        }

    }

    /**
     * 登出
     * 传入的参数中应该包含登出用户的username用来确保不会把别人给登出了
     *
     * @param userLogoutVO 登出的信息，必须包含username
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:56 PM
     */
    @Override
    public String logout(UserLogoutVO userLogoutVO) {
        String msg = "登出成功！";
        UserPOJO user = UserHolder.getUser();

        // 校验登出的username是否为自己
        if (!user.getUsername().equals(userLogoutVO.getUsername())) {
            throw new BaseException("登出用户信息校验失败！");
        }
        // 校验登出的token是否存在
        String token = securityContextPOJO.getGetTokenPrefix() + user.getToken();
        boolean hasKey = redisManager.hasKey(token);
        if (!hasKey) {
            throw new BaseException("登出用户token校验失败！");
        }
        // 删除redis中的jwt作为登出
        redisManager.delete(token);

        return msg;
    }

    /**
     * 更新用户个人信息
     *
     * @param userVO 和注册共用一个吧（懒
     * @return java.lang.String
     * @author Idenn
     * @date 3/30/2024 3:00 AM
     */
    @Override
    public String update(UserUpdateVO userVO) {
        String msg = "更新成功!";
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(userVO, userPO);
        userPO.setPassword(passwordEncoder.encode(userVO.getPassword()));
        if (userMapper.updateById(userPO) > 0) {
            removeUserInfoFromCache(userVO.getObjectId());
            return msg;
        } else {
            throw new BaseException("用户信息更新失败！");
        }
    }

    /**
     * 删除用户
     *
     * @param objectId 包含用户的objectId
     * @return java.lang.String
     * @author Idenn
     * @date 3/30/2024 3:05 AM
     */
    @Override
    public String deleteById(String objectId) {
        String msg = "删除成功！";
        if (userMapper.deleteById(objectId) > 0) {
            removeUserInfoFromCache(objectId);
            return msg;
        } else {
            throw new BaseException("用户信息删除失败！");
        }
    }

    /**
     * 真实删除
     *
     * @param userVO 包含用户的objectId
     * @return java.lang.String
     * @author Idenn
     * @date 3/30/2024 3:08 AM
     */
    @Override
    public String trueDeleteById(UserVO userVO) {
        String msg = "真实删除成功！";
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(userVO, userPO);
        if (trueDelete(userPO) > 0) {
            removeUserInfoFromCache(userVO.getObjectId());
            return msg;
        } else {
            throw new BaseException("用户真实删除失败！");
        }
    }

    /*
     * 从缓存中删除保存的用户信息
     */
    private void removeUserInfoFromCache(String objectId) {
        String redisKey = securityContextPOJO.getUserInfoPrefix() + objectId;
        if (redisManager.hasKey(redisKey)) {
            if (!redisManager.delete(redisKey)) {
                throw new BaseException("缓存信息删除失败！");
            }
        }
    }


    /**
     * 根据用户的object_id获取全部的用户角色、权限、菜单信息
     *
     * @param userVO 查询的用户信息，必须包含object_id
     * @return com.pigeon.entity.user.vo.UserRolesVO
     * @author Idenn
     * @date 3/27/2024 5:04 PM
     */
    @Override
    public UserRolesPermissionsMenusVO getUserWithRolesPermissionsAndMenus(UserVO userVO) {
        String userId = userVO.getObjectId();
        UserRolesPermissionsMenusPO userWithRolesPermissionsAndMenus = userMapper.getUserWithRolesPermissionsAndMenus(userId);
        UserRolesPermissionsMenusVO userRolesVO = new UserRolesPermissionsMenusVO();
        BeanUtils.copyProperties(userWithRolesPermissionsAndMenus, userRolesVO);

        return userRolesVO;
    }

    /**
     * 为用户赋予角色
     *
     * @param userRolesVO 被授权的用户信息，必须包含用户的object_id和角色列表
     * @return java.lang.String
     * @author Idenn
     * @date 3/28/2024 4:41 PM
     */
    @Override
    @Transactional
    public String grantUserRoles(UserRolesVO userRolesVO) {
        String msg = "用户赋予角色成功！";
        List<UserRolesPO> list = new ArrayList<>();
        List<String> distinctList = new ArrayList<>();

        // 手动触发自动填充
        for (RoleVO role : userRolesVO.getRoles()) {
            String userId = userRolesVO.getUserId();
            String roleId = role.getObjectId();

            // 去重
            if (distinctList.contains(roleId)) {
                continue;
            }
            distinctList.add(roleId);

            // 判断用户-角色已经存在
            String roleName = userMapper.getRoleNameByUserRole(userId, roleId);
            if (!StringUtils.isEmpty(roleName)) {
                throw new BaseException("该用户已经拥有【" + roleName + "】角色！");
            }

            // 判断角色是否存在
            if (ObjectUtils.isEmpty(roleMapper.selectById(roleId))) {
                throw new BaseException("id为【" + roleId + "】的角色不存在！");
            }

            // 属性填充
            UserRolesPO userRoles = new UserRolesPO();
            MetaObject metaObject = SystemMetaObject.forObject(userRoles);
            autoFillConfig.insertFill(metaObject);
            userRoles.setUserId(userId);
            userRoles.setRoleId(roleId);
            list.add(userRoles);
        }

        int num = distinctList.size();
        int successCount = userMapper.grantUserRoles(list);
        if (successCount == num) {
            msg += "成功了" + num + "条";
        } else {
            throw new BaseException("用户赋予角色失败！成功了" + successCount + "条，失败了" + (num - successCount) + "条");
        }

        // 移除userInfo
        removeUserInfoFromCache(userRolesVO.getUserId());
        return msg;
    }

}
