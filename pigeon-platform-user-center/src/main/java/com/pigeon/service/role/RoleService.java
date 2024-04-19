package com.pigeon.service.role;

import com.pigeon.entity.role.po.RolePO;
import com.pigeon.entity.role.vo.*;
import com.pigeon.service.BaseService;

import java.util.List;

/**
 * 角色模块实现类
 *
 * @author Idenn
 * @date 3/26/2024 8:85 PM
 */
public interface RoleService extends BaseService<RolePO> {
    List<RoleResultVO> getRoleByPage(RoleQueryVO roleQueryVO);

    String create(RoleVO roleVO);

    String update(RoleVO roleVO);

    String deleteById(String id);

    String trueDeleteById(RoleVO roleVO);

    String grantRolePermissions(RolePermissionsVO rolePermissionsVO);

    String grantRoleMenus(RoleMenusVO roleMenusVO);
}
