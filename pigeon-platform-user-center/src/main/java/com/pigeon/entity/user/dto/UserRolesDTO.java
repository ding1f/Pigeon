package com.pigeon.entity.user.dto;

import com.pigeon.entity.dto.BaseDTO;
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
public class UserRolesDTO extends BaseDTO {

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
