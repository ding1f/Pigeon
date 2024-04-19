package com.pigeon.entity.payment.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.pigeon.entity.dto.BaseDTO;
import com.pigeon.enums.PaymentMethod;
import com.pigeon.enums.PaymentState;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付模块DTO
 *
 * @author Idenn
 * @date 3/7/2024 3:48 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentDTO extends BaseDTO {
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
