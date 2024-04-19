package com.pigeon.enums;

/*
 * 支付状态
 */
public enum PaymentState {

    /*
     * 等待支付中
     */
    PENDING,

    /*
     * 已取消
     */
    CANCEL,

    /*
     * 已支付
     */
    PAYED,

    /*
     * 已超时回滚（未支付，并且商品已恢复）
     */
    TIMEOUT,

    /*
     * 退款请求已提交
     */
    REFUND_REQUESTED,

    /*
     * 退款处理中
     */
    REFUNDING,

    /*
     * 已退款
     */
    REFUNDED,

    /*
     * 退款失败
     */
    REFUND_FAILED
}
