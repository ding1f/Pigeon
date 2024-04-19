package com.pigeon.entity.user.vo;

import com.pigeon.entity.vo.BaseQueryVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserQueryVO
 *
 * @author Idenn
 * @date 3/26/2024 8:56 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryVO extends BaseQueryVO {
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
