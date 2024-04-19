package com.pigeon.entity.role.pojo;

import com.pigeon.entity.menu.pojo.MenuPOJO;
import com.pigeon.entity.permission.pojo.PermissionPOJO;
import com.pigeon.entity.pojo.BasePOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * RolePOJO
 *
 * @author Idenn
 * @date 3/13/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePermissionsMenusPOJO extends BasePOJO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称;角色名称
     */
    private String name;

    /*
     * 属于角色的菜单
     */
    private List<PermissionPOJO> permissions;

    /*
     * 属于角色的菜单
     */
    private List<MenuPOJO> menus;
}
