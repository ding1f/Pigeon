package com.pigeon.entity.permission.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PermissionPO
 *
 * @author Idenn
 * @date 3/13/2024 3:46 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_permission")
public class PermissionPO extends BasePO {

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
