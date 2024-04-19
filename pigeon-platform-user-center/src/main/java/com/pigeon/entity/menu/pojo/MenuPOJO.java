package com.pigeon.entity.menu.pojo;

import com.pigeon.entity.pojo.BasePOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * MenuPOJO
 *
 * @author Idenn
 * @date 3/13/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuPOJO extends BasePOJO {

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
