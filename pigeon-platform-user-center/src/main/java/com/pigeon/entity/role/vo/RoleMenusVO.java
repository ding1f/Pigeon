package com.pigeon.entity.role.vo;

import com.pigeon.entity.menu.vo.MenuVO;
import com.pigeon.entity.vo.BaseVO;
import com.pigeon.utils.ValidGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 赋予角色权限
 *
 * @author Idenn
 * @date 3/29/2024 12:02 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenusVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /*
     * 角色id
     */
    @NotBlank(groups = ValidGroup.Insert.class, message = "需要授权的角色id不能为空！")
    private String roleId;

    /*
     * 角色属于的菜单
     */
    @Size(min = 1, groups = ValidGroup.Insert.class, message = "授权时最少包含一个菜单！")
    private List<MenuVO> menus;
}
