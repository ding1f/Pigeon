package com.pigeon.service.role.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.pigeon.dao.menu.MenuMapper;
import com.pigeon.dao.mybatis.config.AutoFillConfig;
import com.pigeon.dao.permission.PermissionMapper;
import com.pigeon.dao.role.RoleMapper;
import com.pigeon.entity.menu.vo.MenuVO;
import com.pigeon.entity.permission.vo.PermissionVO;
import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.entity.role.po.RoleMenusPO;
import com.pigeon.entity.role.po.RolePO;
import com.pigeon.entity.role.po.RolePermissionsPO;
import com.pigeon.entity.role.vo.*;
import com.pigeon.exception.BaseException;
import com.pigeon.manager.RedisManager;
import com.pigeon.service.impl.BaseServiceImpl;
import com.pigeon.service.role.RoleService;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色模块实现类
 *
 * @author Idenn
 * @date 3/26/2024 8:52 PM
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RolePO> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private AutoFillConfig autoFillConfig;

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    @Resource
    private RedisManager redisManager;

    /**
     * 分页查询
     *
     * @param roleQueryVO 分页查询参数
     * @return java.util.List<com.pigeon.entity.role.vo.RoleResultVO>
     * @author Idenn
     * @date 3/26/2024 10:05 PM
     */
    @Override
    public List<RoleResultVO> getRoleByPage(RoleQueryVO roleQueryVO) {
        return roleMapper.getRoleByPage(roleQueryVO);
    }

    /**
     * 新增角色
     *
     * @param roleVO 角色参数
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:19 PM
     */
    @Override
    public String create(RoleVO roleVO) {
        String msg = "插入成功！";
        RolePO rolePO = new RolePO();
        BeanUtils.copyProperties(roleVO, rolePO);
        if (roleMapper.insert(rolePO) == 1) {
            return msg;
        } else {
            throw new BaseException("角色模块插入失败！");
        }
    }

    /**
     * 更新角色
     *
     * @param roleVO 更新角色的参数
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:20 PM
     */
    @Override
    public String update(RoleVO roleVO) {
        String msg = "更新成功!";
        RolePO rolePO = new RolePO();
        BeanUtils.copyProperties(roleVO, rolePO);
        if (roleMapper.updateById(rolePO) > 0) {
            // 删除这个角色下所有用户的userInfo
            removeUserInfoFromCacheByRoleId(rolePO.getObjectId());
            return msg;
        } else {
            throw new BaseException("角色模块更新失败！");
        }
    }

    /**
     * 删除角色
     *
     * @param id
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @Override
    public String deleteById(String id) {
        String msg = "删除成功！";
        if (roleMapper.deleteById(id) > 0) {
            // 删除这个角色下所有用户的userInfo
            removeUserInfoFromCacheByRoleId(id);
            return msg;
        } else {
            throw new BaseException("角色模块删除失败！");
        }
    }

    /**
     * 真实删除
     *
     * @param roleVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @Override
    public String trueDeleteById(RoleVO roleVO) {
        String msg = "真实删除成功！";
        RolePO rolePO = new RolePO();
        BeanUtils.copyProperties(roleVO, rolePO);
        if (trueDelete(rolePO) > 0) {
            // 删除这个角色下所有用户的userInfo
            removeUserInfoFromCacheByRoleId(rolePO.getObjectId());
            return msg;
        } else {
            throw new BaseException("角色模块真实删除失败！");
        }
    }

    /**
     * 为角色赋予权限
     *
     * @param rolePermissionsVO 被授权的角色信息，必须包含角色的object_id和权限列表
     * @return java.lang.String
     * @author Idenn
     * @date 3/29/2024 1:05 AM
     */
    @Override
    @Transactional
    public String grantRolePermissions(RolePermissionsVO rolePermissionsVO) {
        String msg = "角色赋予权限成功！";
        List<RolePermissionsPO> list = new ArrayList<>();
        List<String> distinctList = new ArrayList<>();
        String roleId = rolePermissionsVO.getRoleId();

        // 手动触发自动填充
        for (PermissionVO permission : rolePermissionsVO.getPermissions()) {
            String permissionId = permission.getObjectId();

            // 去重
            if (distinctList.contains(permissionId)) {
                continue;
            }
            distinctList.add(permissionId);

            // 判断角色-权限已经存在
            String permissionName = roleMapper.getPermissionNameByRolePermission(roleId, permissionId);
            if (!StringUtils.isEmpty(permissionName)) {
                throw new BaseException("该用户已经拥有【" + permissionName + "】权限！");
            }

            // 判断权限是否存在
            if (ObjectUtils.isEmpty(permissionMapper.selectById(permissionId))) {
                throw new BaseException("id为【" + permissionId + "】的权限不存在！");
            }

            // 属性填充
            RolePermissionsPO rolePermissions = new RolePermissionsPO();
            MetaObject metaObject = SystemMetaObject.forObject(rolePermissions);
            autoFillConfig.insertFill(metaObject);
            rolePermissions.setRoleId(roleId);
            rolePermissions.setPermissionId(permissionId);
            list.add(rolePermissions);
        }

        int num = distinctList.size();
        int successCount = roleMapper.grantRolePermissions(list);
        if (successCount == num) {
            msg += "成功了" + num + "条";
            // 删除这个角色下所有用户的userInfo
            removeUserInfoFromCacheByRoleId(roleId);
        } else {
            throw new BaseException("角色赋予权限失败！成功了" + successCount + "条，失败了" + (num - successCount) + "条");
        }
        return msg;
    }

    /**
     * 为角色赋予菜单
     *
     * @param roleMenusVO 被授权的角色信息，必须包含角色的object_id和菜单列表
     * @return java.lang.String
     * @author Idenn
     * @date 3/29/2024 2:56 AM
     */
    @Override
    @Transactional
    public String grantRoleMenus(RoleMenusVO roleMenusVO) {
        String msg = "角色赋予菜单成功！";
        List<RoleMenusPO> list = new ArrayList<>();
        List<String> distinctList = new ArrayList<>();
        String roleId = roleMenusVO.getRoleId();

        // 手动触发自动填充
        for (MenuVO menu : roleMenusVO.getMenus()) {
            String menuId = menu.getObjectId();

            // 去重
            if (distinctList.contains(menuId)) {
                continue;
            }
            distinctList.add(menuId);

            // 判断角色-菜单已经存在
            String menuName = roleMapper.getMenuNameByRoleMenu(roleId, menuId);
            if (!StringUtils.isEmpty(menuName)) {
                throw new BaseException("该用户已经拥有【" + menuName + "】菜单！");
            }

            // 判断菜单是否存在
            if (ObjectUtils.isEmpty(menuMapper.selectById(menuId))) {
                throw new BaseException("id为【" + menuId + "】的菜单不存在！");
            }

            // 属性填充
            RoleMenusPO roleMenus = new RoleMenusPO();
            MetaObject metaObject = SystemMetaObject.forObject(roleMenus);
            autoFillConfig.insertFill(metaObject);
            roleMenus.setRoleId(roleId);
            roleMenus.setMenuId(menuId);
            list.add(roleMenus);
        }

        int num = distinctList.size();
        int successCount = roleMapper.grantRoleMenus(list);
        if (successCount == num) {
            msg += "成功了" + num + "条";
            // 删除这个角色下所有用户的userInfo
            removeUserInfoFromCacheByRoleId(roleId);
        } else {
            throw new BaseException("角色赋予菜单失败！成功了" + successCount + "条，失败了" + (num - successCount) + "条");
        }
        return msg;
    }

    /*
     * 从缓存中删除保存的用户信息
     */
    private void removeUserInfoFromCacheByRoleId(String roleId) {
        List<String> userIds = roleMapper.getUserObjectIdByRoleId(roleId);
        for (String userId : userIds) {
            String redisKey = securityContextPOJO.getUserInfoPrefix() + userId;
            if (redisManager.hasKey(redisKey)) {
                if (!redisManager.delete(redisKey)) {
                    throw new BaseException("缓存信息删除失败！");
                }
            }
        }
    }


}
