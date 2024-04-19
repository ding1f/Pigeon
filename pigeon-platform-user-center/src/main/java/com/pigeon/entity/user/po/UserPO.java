package com.pigeon.entity.user.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.po.BasePO;
import com.pigeon.interfaces.annotation.IsUnique;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserPO
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_user")
public class UserPO extends BasePO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名;用户名
     */
    @IsUnique(name = "用户名")
    private String username;
    /**
     * 密码;密码
     */
    private String password;
    /**
     * 电话;电话
     */
    @IsUnique(name = "手机号码")
    private String telephone;
    /**
     * 邮箱;邮箱
     */
    @IsUnique(name = "邮箱")
    private String email;
    /**
     * 地址;location
     */
    private String location;
}
