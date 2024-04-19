package com.pigeon.entity.payment.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pigeon.entity.vo.BaseVO;
import com.pigeon.enums.PaymentMethod;
import com.pigeon.enums.PaymentState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Payment模块查询返回结果
 *
 * @author Idenn
 * @date 3/11/2024 9:17 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("custom_payment")
public class PaymentResultVO extends BaseVO {
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
