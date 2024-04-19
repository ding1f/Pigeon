package com.pigeon.entity.menu.dto;

import com.pigeon.entity.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * public class MenuDTO extends BaseDTO {
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称;菜单名称
     */
    private String name;

    /**
     * 父级菜单ID;父级菜单ID
     */
    private String parentId;

}
