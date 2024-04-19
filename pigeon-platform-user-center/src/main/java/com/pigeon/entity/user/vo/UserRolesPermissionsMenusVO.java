package com.pigeon.entity.user.vo;

import com.pigeon.entity.role.vo.RolePermissionsMenusVO;
import com.pigeon.entity.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * UserRolesVO 用来关联用户和角色
 *
 * @author Idenn
 * @date 3/27/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRolesPermissionsMenusVO extends BaseVO {

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
    private List<RolePermissionsMenusVO> roles;

}
