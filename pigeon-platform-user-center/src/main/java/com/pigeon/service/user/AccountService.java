package com.pigeon.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigeon.entity.dto.UserDTO;
import com.pigeon.entity.user.po.UserPO;
import com.pigeon.entity.user.vo.*;
import com.pigeon.service.BaseService;

import java.util.List;

/**
 * 用户模块实现类
 *
 * @author Idenn
 * @date 3/13/2024 6:01 PM
 */
public interface AccountService extends BaseService<UserPO> {

    List<UserResultVO> getAccountByPage(UserQueryVO userQueryVO);

    String register(UserRegisterVO userRegisterVO);

    UserDTO getUserByUsername(String name);

    String login(UserLoginVO userLoginVO) throws Exception;

    String logout(UserLogoutVO userLogoutVO);

    String update(UserUpdateVO userVO);

    String deleteById(String objectId);

    String trueDeleteById(UserVO userVO);

    UserRolesPermissionsMenusVO getUserWithRolesPermissionsAndMenus(UserVO userVO);

    String grantUserRoles(UserRolesVO userRolesVO);
}
