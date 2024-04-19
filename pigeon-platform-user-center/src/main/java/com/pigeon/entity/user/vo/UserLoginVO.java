package com.pigeon.entity.user.vo;

import com.pigeon.entity.vo.BaseVO;
import com.pigeon.utils.ValidGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

/**
 * 登录用VO
 *
 * @author Idenn
 * @date 3/21/2024 12:46 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserLoginVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名;用户名
     */
    @NotBlank(groups = ValidGroup.Insert.class, message = "用户名不能为空！")
    private String username;

    /**
     * 密码;密码
     */
    @NotBlank(groups = ValidGroup.Insert.class, message = "密码不能为空！")
    private String password;
}