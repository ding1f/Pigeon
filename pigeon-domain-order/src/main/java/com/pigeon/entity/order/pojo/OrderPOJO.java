package com.pigeon.entity.order.pojo;

import com.pigeon.entity.pojo.BasePOJO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * OrderPOJO
 *
 * @author Idenn
 * @date 4/2/2024 9:07 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderPOJO extends BasePOJO {
    private static final long serialVersionUID = 1L;

    /**
     * 下单的用户ID;下单的用户ID
     */
    private String userId;

    /**
     * 订单的总价;订单的总价
     */
    private BigDecimal totalPrice;

    /**
     * 订单状态;订单状态
     */
    private String status;
}
