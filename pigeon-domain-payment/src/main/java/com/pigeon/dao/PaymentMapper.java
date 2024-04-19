package com.pigeon.dao;

import com.pigeon.entity.payment.po.PaymentPO;
import com.pigeon.entity.payment.vo.PaymentQueryVO;
import com.pigeon.entity.payment.vo.PaymentResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper extends CustomMapper<PaymentPO> {

    /*
     * 分页查询支付信息
     */
    List<PaymentResultVO> getPaymentByPage(PaymentQueryVO paymentQueryVO);
}
