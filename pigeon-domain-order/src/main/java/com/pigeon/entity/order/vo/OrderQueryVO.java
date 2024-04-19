package com.pigeon.entity.order.vo;

import com.pigeon.entity.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * OrderVO
 *
 * @author Idenn
 * @date 4/2/2024 9:07 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderQueryVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    /**
     * 下单的用户ID;下单的用户ID
     */
    private String userId;

    /**
     * 订单状态;订单状态
     */
    private String status;
}
