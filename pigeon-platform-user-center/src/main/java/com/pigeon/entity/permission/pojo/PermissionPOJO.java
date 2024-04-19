package com.pigeon.entity.permission.pojo;

import com.pigeon.entity.pojo.BasePOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PermissionPOJO
 *
 * @author Idenn
 * @date 3/13/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionPOJO extends BasePOJO {

    private static final long serialVersionUID = 1L;

    /**
     * 权限名称;权限名称
     */
    private String name;

    /**
     * 权限描述;权限描述
     */
    private String description;
}
