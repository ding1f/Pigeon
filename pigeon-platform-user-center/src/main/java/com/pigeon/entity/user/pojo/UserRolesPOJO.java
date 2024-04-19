package com.pigeon.entity.user.pojo;

import com.pigeon.entity.pojo.BasePOJO;
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
public class UserRolesPOJO extends BasePOJO {

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
