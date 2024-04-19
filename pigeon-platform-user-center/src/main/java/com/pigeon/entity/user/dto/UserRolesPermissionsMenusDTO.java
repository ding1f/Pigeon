package com.pigeon.entity.user.dto;

import com.pigeon.entity.dto.BaseDTO;
import com.pigeon.entity.role.dto.RolePermissionsMenusDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * userDTO
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRolesPermissionsMenusDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名;用户名
     */
    private String username;

    /**
     * 密码;密码
     */
    private String password;

    /**
     * 电话;电话
     */
    private String telephone;

    /**
     * 邮箱;邮箱
     */
    private String email;

    /**
     * 地址;location
     */
    private String location;

    /*
     * 用户属于的角色
     */
    private List<RolePermissionsMenusDTO> roles;

}
