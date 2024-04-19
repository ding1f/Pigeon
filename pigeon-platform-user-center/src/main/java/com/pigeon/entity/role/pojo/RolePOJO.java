package com.pigeon.entity.role.pojo;

import com.pigeon.entity.pojo.BasePOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RolePOJO
 *
 * @author Idenn
 * @date 3/13/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePOJO extends BasePOJO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称;角色名称
     */
    private String name;
}
