package com.pigeon.entity.menu.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * MenuVO
 *
 * @author Idenn
 * @date 3/13/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_menu")
public class MenuResultVO extends BaseVO {

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
