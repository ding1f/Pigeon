package com.pigeon.service;

import com.pigeon.entity.payment.po.PaymentPO;
import com.pigeon.entity.payment.vo.PaymentQueryVO;
import com.pigeon.entity.payment.vo.PaymentResultVO;
import com.pigeon.entity.payment.vo.PaymentVO;

import java.util.List;

/**
 * 支付模块实现类
 *
 * @author Idenn
 * @date 3/6/2024 6:01 PM
 */
public interface PaymentService extends BaseService<PaymentPO> {
    PaymentVO getById(String id);

    String create(PaymentVO paymentVO);

    String deleteById(String id);

    String update(PaymentVO paymentVO);

    List<PaymentResultVO> getPaymentByPage(PaymentQueryVO paymentQueryVO);

    String trueDeleteById(PaymentVO paymentVO);

    String createPaymentByOrder(String orderId);
}
