package com.pigeon.entity.user.vo;

import com.pigeon.entity.vo.BaseVO;
import com.pigeon.utils.ValidGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 注册用VO
 *
 * @author Idenn
 * @date 3/16/2024 4:39 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRegisterVO extends BaseVO {

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

    /**
     * 电话;电话
     */
    @NotBlank(groups = ValidGroup.Insert.class, message = "电话号码不能为空！")
    @Length(groups = ValidGroup.Insert.class, min = 11, max = 11, message = "电话号码格式错误！")
    private String telephone;

    /**
     * 邮箱;邮箱
     */
    @NotBlank(groups = ValidGroup.Insert.class, message = "邮箱不能为空！")
    @Email(groups = ValidGroup.Insert.class, message = "邮箱格式错误！")
    private String email;

    /**
     * 地址;location
     */
    private String location;
}