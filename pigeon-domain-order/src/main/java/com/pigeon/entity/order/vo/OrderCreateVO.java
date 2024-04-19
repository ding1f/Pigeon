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
public class OrderCreateVO extends BaseVO {
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
    private State status;

    /**
     * 订单状态
     */
    public enum State {
        /**
         * 等待支付中
         */
        WAITING,
        /**
         * 已取消
         */
        CANCEL,
        /**
         * 已支付
         */
        PAYED,
        /**
         * 已超时回滚（未支付，并且商品已恢复）
         */
        TIMEOUT
    }
}
