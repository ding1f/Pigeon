package com.pigeon.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * PO (Persistent Object)：持久化对象，用于表示数据库中的一条记录，通常和数据库表一一对应。
 *
 * @author Idenn
 * @date 3/4/2024 7:19 PM
 */
@Data
public abstract class BasePO implements Serializable {

    /**
     * base-ID;主键
     */
    @TableId(value = "object_id", type = IdType.INPUT)
    private String objectId;

    /**
     * base-是否删除;0未删除 1已删除
     */
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private String isDeleted;

    /**
     * base-乐观锁;乐观锁
     */
    @Version
    @TableField(value = "revision", fill = FieldFill.INSERT)
    private Integer revision;

    /**
     * base-创建人;创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * base-创建时间;创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * base-更新人;更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * base-更新时间;更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;
}
