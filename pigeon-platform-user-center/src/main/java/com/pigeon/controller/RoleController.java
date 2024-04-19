package com.pigeon.controller;

import com.pigeon.entity.role.vo.*;
import com.pigeon.service.role.RoleService;
import com.pigeon.utils.ValidGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色controller
 *
 * @author Idenn
 * @date 3/26/2024 8:50 PM
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 分页查询
     *
     * @param roleQueryVO
     * @return java.util.List<com.pigeon.entity.role.vo.RoleResultVO>
     * @author Idenn
     * @date 3/26/2024 10:05 PM
     */
    @GetMapping("/getRoleByPage")
    public List<RoleResultVO> getRoleByPage(@RequestBody RoleQueryVO roleQueryVO) {
        return roleService.getRoleByPage(roleQueryVO);
    }

    /**
     * 新增角色
     *
     * @param roleVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:19 PM
     */
    @PostMapping("/create")
    public String create(@RequestBody RoleVO roleVO) {
        return roleService.create(roleVO);
    }

    /**
     * 更新角色
     *
     * @param roleVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:20 PM
     */
    @PostMapping("/update")
    public String update(@RequestBody RoleVO roleVO) {
        return roleService.update(roleVO);
    }

    /**
     * 删除角色
     *
     * @param roleVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @DeleteMapping("/deleteById")
    public String deleteById(@RequestBody RoleVO roleVO) {
        String id = roleVO.getObjectId();
        return roleService.deleteById(id);
    }

    /**
     * 真实删除
     *
     * @param roleVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @DeleteMapping("/trueDeleteById")
    public String trueDeleteById(@RequestBody RoleVO roleVO) {
        return roleService.trueDeleteById(roleVO);
    }

    /**
     * 为角色赋予权限
     *
     * @param rolePermissionsVO 被授权的角色信息，必须包含角色的object_id和权限列表
     * @return java.lang.String
     * @author Idenn
     * @date 3/29/2024 1:05 AM
     */
    @PostMapping("/grantRolePermissions")
    public String grantRolePermissions(@RequestBody @Validated(ValidGroup.Insert.class) RolePermissionsVO rolePermissionsVO) {
        return roleService.grantRolePermissions(rolePermissionsVO);
    }

    /**
     * 为角色赋予菜单
     *
     * @param roleMenusVO 被授权的角色信息，必须包含角色的object_id和菜单列表
     * @return java.lang.String
     * @author Idenn
     * @date 3/29/2024 2:56 AM
     */
    @PostMapping("/grantRoleMenus")
    public String grantRoleMenus(@RequestBody @Validated(ValidGroup.Insert.class) RoleMenusVO roleMenusVO) {
        return roleService.grantRoleMenus(roleMenusVO);
    }

}
