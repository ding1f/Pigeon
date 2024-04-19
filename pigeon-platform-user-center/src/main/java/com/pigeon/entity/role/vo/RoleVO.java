package com.pigeon.entity.role.vo;

import com.pigeon.entity.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RoleVO
 *
 * @author Idenn
 * @date 3/13/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称;角色名称
     */
    private String name;
}
