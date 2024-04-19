package com.pigeon.entity.role.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.menu.po.MenuPO;
import com.pigeon.entity.permission.po.PermissionPO;
import com.pigeon.entity.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * RolePO
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_role")
public class RolePermissionsMenusPO extends BasePO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称;角色名称
     */
    private String name;

    /*
     * 属于角色的菜单
     */
    private List<PermissionPO> permissions;

    /*
     * 属于角色的菜单
     */
    private List<MenuPO> menus;
}
