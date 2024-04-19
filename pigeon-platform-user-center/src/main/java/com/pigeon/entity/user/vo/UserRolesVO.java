package com.pigeon.entity.user.vo;

import com.pigeon.entity.role.vo.RoleVO;
import com.pigeon.entity.vo.BaseVO;
import com.pigeon.utils.ValidGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 赋予用户角色
 *
 * @author Idenn
 * @date 3/27/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRolesVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /*
     * 用户id
     */
    @NotBlank(groups = ValidGroup.Insert.class, message = "需要授权的用户id不能为空！")
    private String userId;

    /*
     * 用户属于的角色
     */
    @Size(min = 1, groups = ValidGroup.Insert.class, message = "授权时最少包含一个角色！")
    private List<RoleVO> roles;

}
