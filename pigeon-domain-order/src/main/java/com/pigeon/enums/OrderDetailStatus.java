package com.pigeon.enums;

/*
 * 订单明细状态
 */
public enum OrderDetailStatus {

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
