package com.pigeon.entity.role.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 赋予角色权限
 *
 * @author Idenn
 * @date 3/29/2024 12:02 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_role_menus")
public class RoleMenusPO extends BasePO {

    private static final long serialVersionUID = 1L;

    /*
     * 角色id
     */
    private String roleId;

    /*
     * 菜单id
     */
    private String menuId;
}
