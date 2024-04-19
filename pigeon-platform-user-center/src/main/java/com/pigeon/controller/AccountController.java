package com.pigeon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigeon.entity.dto.UserDTO;
import com.pigeon.entity.user.vo.*;
import com.pigeon.interfaces.annotation.NoControllerResponseAdvice;
import com.pigeon.response.CommonResponse;
import com.pigeon.service.user.AccountService;
import com.pigeon.service.user.UserInfoService;
import com.pigeon.utils.ValidGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户模块controller
 *
 * @author Idenn
 * @date 3/13/2024 3:42 PM
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 分页查询用户信息
     *
     * @param userQueryVO
     * @return java.util.List<com.pigeon.entity.user.vo.UserResultVO>
     * @author Idenn
     * @date 3/26/2024 9:01 PM
     */
    @GetMapping("/getAccountByPage")
    public List<UserResultVO> getAccountByPage(@RequestBody UserQueryVO userQueryVO) {
        return accountService.getAccountByPage(userQueryVO);
    }

    /**
     * 注册
     *
     * @param userRegisterVO 注册用信息
     * @return java.lang.String
     * @author Idenn
     * @date 3/16/2024 5:55 PM
     */
    @PostMapping("/register")
    public String register(@RequestBody @Validated(ValidGroup.Insert.class) UserRegisterVO userRegisterVO) {
        return accountService.register(userRegisterVO);
    }

    /**
     * feign接口，根据用户名获取用户信息
     *
     * @param name 用户名
     * @return com.pigeon.entity.dto.UserDTO
     * @author Idenn
     * @date 3/20/2024 8:37 PM
     */
    @GetMapping("/getUserByUsername")
    public UserDTO getUserByUsername(String name) {
        return accountService.getUserByUsername(name);
    }

    /**
     * 根据用户名登录
     * feat: 根据倒排索引，可以使用邮箱/手机号进行登录
     *
     * @param userLoginVO 登录用VO，传入的username字段也可能是邮箱或者手机号
     * @return java.lang.String
     * @author Idenn
     * @date 3/21/2024 1:20 AM
     */
    @NoControllerResponseAdvice
    @PostMapping("/login")
    public String login(@RequestBody @Validated(ValidGroup.Insert.class) UserLoginVO userLoginVO) throws Exception {
        return accountService.login(userLoginVO);
    }


    /**
     * 登出
     * 传入的参数中应该包含登出用户的username用来确保不会把别人给登出了
     * 已被废弃，使用security自带的logoutHandler进行登出操作
     *
     * @param userLogoutVO 登出的信息，必须包含username
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:56 PM
     */
    @PostMapping("/logout")
    public String logout(@RequestBody @Validated(ValidGroup.Insert.class) UserLogoutVO userLogoutVO) {
        return accountService.logout(userLogoutVO);
    }

    /**
     * 更新用户个人信息
     * todo 这里理论上应该区分管理员修改信息和个人修改信息，个人修改信息应该通过UserHolder判断是否修改的是自己
     *
     * @param userVO 不包含username（不可修改）其他的和注册一样
     * @return java.lang.String
     * @author Idenn
     * @date 3/30/2024 3:00 AM
     */
    @PostMapping("/update")
    public String update(@RequestBody @Validated(ValidGroup.Update.class) UserUpdateVO userVO) {
        return accountService.update(userVO);
    }

    /**
     * 删除用户
     * todo 同修改
     *
     * @param userVO 包含用户的objectId
     * @return java.lang.String
     * @author Idenn
     * @date 3/30/2024 3:05 AM
     */
    @DeleteMapping("/deleteById")
    public String deleteById(@RequestBody UserVO userVO) {
        String objectId = userVO.getObjectId();
        return accountService.deleteById(objectId);
    }

    /**
     * 真实删除
     *
     * @param userVO 包含用户的objectId
     * @return java.lang.String
     * @author Idenn
     * @date 3/30/2024 3:08 AM
     */
    @DeleteMapping("/trueDeleteById")
    public String trueDeleteById(@RequestBody UserVO userVO) {
        return accountService.trueDeleteById(userVO);
    }

    /**
     * 根据用户的object_id获取全部的用户角色、权限、菜单信息
     *
     * @param userVO 查询的用户信息，必须包含object_id
     * @return com.pigeon.entity.user.vo.UserRolesVO
     * @author Idenn
     * @date 3/27/2024 5:04 PM
     */
    @GetMapping("/getAccountInfoById")
    public UserRolesPermissionsMenusVO getAccountInfoById(@RequestBody @Validated(ValidGroup.Select.class) UserVO userVO) {
        return accountService.getUserWithRolesPermissionsAndMenus(userVO);
    }

    /**
     * 为用户赋予角色
     *
     * @param userRolesVO 被授权的用户信息，必须包含用户的object_id和角色列表
     * @return java.lang.String
     * @author Idenn
     * @date 3/28/2024 4:41 PM
     */
    @PostMapping("/grantUserRoles")
    public String grantUserRoles(@RequestBody @Validated(ValidGroup.Insert.class) UserRolesVO userRolesVO) {
        return accountService.grantUserRoles(userRolesVO);
    }

    /**
     * 从cache中获取userInfo
     *
     * @param objectId userId
     * @return com.pigeon.entity.dto.UserDTO
     * @author Idenn
     * @date 3/29/2024 7:46 PM
     */
    @PostMapping("/fetchUserInfoFromCache")
    public UserDTO fetchUserInfoFromCache(String objectId) throws JsonProcessingException {
        return userInfoService.getUserInfoFromCache(objectId);
    }

    /**
     * 从cache中获取权限列表
     * 这里因为返回为List类型的会被默认为分页查询，所以直接手动范围CommonResponse
     *
     * @param objectId userId
     * @return java.util.List<java.lang.String>
     * @author Idenn
     * @date 3/29/2024 7:46 PM
     */
    @PostMapping("/fetchPermissionsFromCache")
    public CommonResponse<List<String>> fetchPermissionsFromCache(String objectId) throws JsonProcessingException {
        List<String> permissionsFromCache = userInfoService.getPermissionsFromCache(objectId);
        return new CommonResponse<>(permissionsFromCache);
    }

}
