package com.pigeon.entity.permission.dto;

import com.pigeon.entity.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PermissionDTO
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionDTO extends BaseDTO {

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
