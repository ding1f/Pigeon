package com.pigeon.dao.user;

import com.pigeon.dao.CustomMapper;
import com.pigeon.entity.user.po.UserPO;
import com.pigeon.entity.user.po.UserRolesPO;
import com.pigeon.entity.user.po.UserRolesPermissionsMenusPO;
import com.pigeon.entity.user.vo.UserQueryVO;
import com.pigeon.entity.user.vo.UserResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends CustomMapper<UserPO> {

    /*
     * 分页查询用户信息
     */
    List<UserResultVO> getAccountByPage(UserQueryVO userQueryVO);

    /*
     * 通过用户名获取用户
     */
    UserPO getUserByUsername(String name);

    /*
     * 通过用户名或者邮箱或者手机号获取用户
     */
    UserPO getUserByUsernameOrEmailOrTelephone(String name);

    /*
     * 通过id获取用户
     */
    UserPO getUserById(String id);

    /*
     * 获取用户属于的角色以及权限和菜单信息
     */
    UserRolesPermissionsMenusPO getUserWithRolesPermissionsAndMenus(String userId);

    /*
     * 授予用户权限
     */
    int grantUserRoles(List<UserRolesPO> userRoles);

    /*
     * 通过用户Id+角色Id获取角色名
     */
    String getRoleNameByUserRole(@Param(value = "userId") String userId, @Param(value = "roleId") String roleId);
}
