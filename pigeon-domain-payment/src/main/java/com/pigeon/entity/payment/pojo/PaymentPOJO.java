package com.pigeon.entity.payment.pojo;

import com.pigeon.entity.pojo.BasePOJO;
import com.pigeon.enums.PaymentMethod;
import com.pigeon.enums.PaymentState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 支付模块的POJO
 *
 * @author Idenn
 * @date 3/7/2024 3:53 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentPOJO extends BasePOJO {
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID;关联的订单ID
     */
    private String orderId;
    /**
     * 用户ID;发起支付的用户ID
     */
    private String userId;
    /**
     * 金额;支付金额
     */
    private BigDecimal amount;
    /**
     * 支付状态;支付状态
     */
    private PaymentState status;
    /**
     * 支付方式;支付方式
     */
    private PaymentMethod method;
}
