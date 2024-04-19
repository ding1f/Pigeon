package com.pigeon.entity.role.dto;

import com.pigeon.entity.dto.BaseDTO;
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
public class RoleMenusDTO extends BaseDTO {

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
