package com.pigeon.entity.order.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.po.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * OrderPO
 *
 * @author Idenn
 * @date 4/2/2024 9:07 PM
 */
@Data
@TableName("custom_order")
@EqualsAndHashCode(callSuper = true)
public class OrderPO extends BasePO {
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
