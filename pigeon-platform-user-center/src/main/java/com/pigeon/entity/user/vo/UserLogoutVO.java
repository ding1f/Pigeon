package com.pigeon.entity.user.vo;

import com.pigeon.entity.vo.BaseVO;
import com.pigeon.utils.ValidGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 登出VO
 *
 * @author Idenn
 * @date 3/26/2024 10:57 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserLogoutVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名;用户名
     */
    @NotBlank(groups = ValidGroup.Insert.class, message = "用户名不能为空！")
    private String username;
}