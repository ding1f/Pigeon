package com.pigeon.entity.role.vo;

import com.pigeon.entity.vo.BaseQueryVO;
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
public class RoleQueryVO extends BaseQueryVO {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称;角色名称
     */
    private String name;
}
