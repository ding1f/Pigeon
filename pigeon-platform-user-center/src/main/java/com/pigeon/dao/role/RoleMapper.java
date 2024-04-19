package com.pigeon.dao.role;

import com.pigeon.dao.CustomMapper;
import com.pigeon.entity.role.po.RoleMenusPO;
import com.pigeon.entity.role.po.RolePO;
import com.pigeon.entity.role.po.RolePermissionsPO;
import com.pigeon.entity.role.vo.RoleQueryVO;
import com.pigeon.entity.role.vo.RoleResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends CustomMapper<RolePO> {

    /*
     * 分页查询角色
     */
    List<RoleResultVO> getRoleByPage(RoleQueryVO RoleQueryVO);

    /*
     * 通过角色id+权限id获取权限名
     */
    String getPermissionNameByRolePermission(@Param(value = "roleId") String roleId, @Param(value = "permissionId") String permissionId);

    /*
     * 授予角色权限
     */
    int grantRolePermissions(List<RolePermissionsPO> rolePermissions);

    /*
     * 通过角色id+菜单id获取菜单名
     */
    String getMenuNameByRoleMenu(@Param(value = "roleId") String roleId, @Param(value = "menuId") String menuId);

    /*
     * 授予角色菜单
     */
    int grantRoleMenus(List<RoleMenusPO> roleMenus);

    /*
     * 通过角色id获取属于它的用户id
     */
    List<String> getUserObjectIdByRoleId(String id);
}
