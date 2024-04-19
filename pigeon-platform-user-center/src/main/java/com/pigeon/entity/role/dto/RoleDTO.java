package com.pigeon.entity.role.dto;

import com.pigeon.entity.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RoleDTO
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称;角色名称
     */
    private String name;

}
