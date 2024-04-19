package com.pigeon.entity.user.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserResultVO
 *
 * @author Idenn
 * @date 3/26/2024 8:57 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_user")
public class UserResultVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名;用户名
     */
    private String username;

    /**
     * 电话;电话
     */
    private String telephone;

    /**
     * 邮箱;邮箱
     */
    private String email;

    /**
     * 地址;location
     */
    private String location;

}
