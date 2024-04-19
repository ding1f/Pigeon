package com.pigeon.entity.permission.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PermissionVO
 *
 * @author Idenn
 * @date 3/13/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_permission")
public class PermissionResultVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 权限名称;权限名称
     */
    private String name;

    /**
     * 权限描述;权限描述
     */
    private String description;
}
