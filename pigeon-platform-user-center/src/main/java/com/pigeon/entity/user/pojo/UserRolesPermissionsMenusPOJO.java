package com.pigeon.entity.user.pojo;

import com.pigeon.entity.pojo.BasePOJO;
import com.pigeon.entity.role.pojo.RolePermissionsMenusPOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * UserRolesPOJO 用来关联用户和角色
 *
 * @author Idenn
 * @date 3/27/2024 3:51 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRolesPermissionsMenusPOJO extends BasePOJO {

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
    private List<RolePermissionsMenusPOJO> roles;

}
