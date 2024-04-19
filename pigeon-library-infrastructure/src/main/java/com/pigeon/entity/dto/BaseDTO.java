package com.pigeon.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO (Data Transfer Object)数据传输对象
 *
 * @author Idenn
 * @date 3/4/2024 4:17 PM
 */
@Data
public abstract class BaseDTO implements Serializable {
    /**
     * base-ID;主键
     */
    private String objectId;
    /**
     * base-是否删除;0未删除 1已删除
     */
    private String isDeleted;
    /**
     * base-乐观锁;乐观锁
     */
    private Integer revision;
    /**
     * base-创建人;创建人
     */
    private String createdBy;
    /**
     * base-创建时间;创建时间
     */
    private Date createdTime;
    /**
     * base-更新人;更新人
     */
    private String updatedBy;
    /**
     * base-更新时间;更新时间
     */
    private Date updatedTime;
}
