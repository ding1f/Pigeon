package com.pigeon.entity.user.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关系
 *
 * @author Idenn
 * @date 3/28/2024 8:00 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_user_roles")
public class UserRolesPO extends BasePO {

    private static final long serialVersionUID = 1L;

    /*
     * 用户id
     */
    private String userId;

    /*
     * 角色id
     */
    private String roleId;
}
