package com.pigeon.entity.payment.vo;

import com.pigeon.entity.vo.BaseQueryVO;
import com.pigeon.enums.PaymentMethod;
import com.pigeon.enums.PaymentState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Payment模块查询VO
 * 需要排序的字段也应该包含在里面
 *
 * @author Idenn
 * @date 3/11/2024 9:03 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentQueryVO extends BaseQueryVO {
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
