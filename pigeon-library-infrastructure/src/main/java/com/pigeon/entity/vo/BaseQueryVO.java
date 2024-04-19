package com.pigeon.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础查询VO
 *
 * @author Idenn
 * @date 3/11/2024 8:19 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseQueryVO extends BaseVO {

    /**
     * 页码
     */
    private Integer pageNum;
    /*
     * 每页记录数
     */
    private Integer pageSize;
    /*
     * 排序字段
     */
    private String orderColumn;
    /*
     * 排序规则
     */
    private String orderSort;

}
