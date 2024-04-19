package com.pigeon.enums;

/*
 * 订单状态
 */
public enum OrderStatus {

    /*
     * 待支付
     */
    PENDING,

    /*
     * 已支付
     */
    PAYED,

    /*
     * 待发货
     */
    AWAITING_SHIPMENT,

    /*
     * 发货中
     * 有一件发货了那么就变成发货中
     */
    SHIPPING,

    /*
     * 退款请求已提交
     */
    REFUND_REQUESTED,

    /*
     * 退款中
     */
    REFUNDING,

    /*
     * 已退款
     */
    REFUNDED,

    /*
     * 退货请求已提交
     */
    RETURN_REQUESTED,

    /*
     * 退货中
     */
    RETURNING,

    /*
     * 已退货
     */
    RETURNED

}
