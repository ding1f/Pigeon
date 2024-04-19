package com.pigeon.entity.menu.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * MenuPO
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_menu")
public class MenuPO extends BasePO {

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
