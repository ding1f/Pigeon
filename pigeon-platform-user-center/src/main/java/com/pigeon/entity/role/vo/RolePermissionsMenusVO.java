package com.pigeon.entity.role.vo;

import com.pigeon.entity.menu.vo.MenuVO;
import com.pigeon.entity.permission.vo.PermissionVO;
import com.pigeon.entity.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * RoleVO
 *
 * @author Idenn
 * @date 3/13/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePermissionsMenusVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称;角色名称
     */
    private String name;

    /*
     * 属于角色的权限
     */
    private List<PermissionVO> permissions;

    /*
     * 属于角色的菜单
     */
    private List<MenuVO> menus;
}
