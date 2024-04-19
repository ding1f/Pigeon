package com.pigeon.entity.role.dto;

import com.pigeon.entity.dto.BaseDTO;
import com.pigeon.entity.menu.dto.MenuDTO;
import com.pigeon.entity.permission.dto.PermissionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * RoleDTO
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePermissionsMenusDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称;角色名称
     */
    private String name;

    /*
     * 属于角色的菜单
     */
    private List<PermissionDTO> permissions;

    /*
     * 属于角色的菜单
     */
    private List<MenuDTO> menus;

}
