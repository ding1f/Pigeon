package com.pigeon.service.impl;

import com.pigeon.context.UserHolder;
import com.pigeon.entity.payment.po.PaymentPO;
import com.pigeon.entity.payment.vo.PaymentQueryVO;
import com.pigeon.entity.payment.vo.PaymentResultVO;
import com.pigeon.entity.payment.vo.PaymentVO;
import com.pigeon.exception.BaseException;
import com.pigeon.dao.PaymentMapper;
import com.pigeon.service.PaymentService;
import io.seata.core.context.RootContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 支付模块实现类
 *s
 * @author Idenn
 * @date 3/6/2024 6:01 PM
 */
@Service
public class PaymentServiceImpl extends BaseServiceImpl<PaymentPO> implements PaymentService {

    @Resource
    private PaymentMapper paymentMapper;

    /**
     * 分页查询
     *
     * @param paymentQueryVO 查询的参数
     * @return com.pigeon.entity.vo.PaymentResultVO
     * @author Idenn
     * @date 3/11/2024 9:19 PM
     */
    @Override
    public List<PaymentResultVO> getPaymentByPage(PaymentQueryVO paymentQueryVO) {
        return paymentMapper.getPaymentByPage(paymentQueryVO);
    }

    /**
     * 通过id查询支付信息
     * @param id 查询id
     * @return 查询结果
     */
    @Override
    public PaymentVO getById(String id) {
        PaymentVO paymentVO = new PaymentVO();
        PaymentPO paymentPO = paymentMapper.selectById(id);
        if (paymentPO != null) {
            BeanUtils.copyProperties(paymentPO, paymentVO);
            return paymentVO;
        } else {
            return null;
        }
    }

    /**
     * 插入支付信息
     *
     * @param paymentVO 支付信息
     * @return int
     * @author Idenn
     * @date 3/7/2024 4:43 PM
     */
    @Override
    public String create(PaymentVO paymentVO) {
        String msg = "插入成功！";
        PaymentPO paymentPO = new PaymentPO();
        BeanUtils.copyProperties(paymentVO, paymentPO);
        if (paymentMapper.insert(paymentPO) == 1) {
            return msg;
        } else {
            throw new BaseException("支付模块插入失败！");
        }
    }

    /**
     * 通过id删除支付记录
     *
     * @param id 操作id
     * @return java.lang.String
     * @author Idenn
     * @date 3/7/2024 8:09 PM
     */
    @Override
    public String deleteById(String id) {
        String msg = "删除成功！";
        if (paymentMapper.deleteById(id) > 0) {
            return msg;
        } else {
            throw new BaseException("支付模块删除失败！");
        }
    }

    /**
     * 更新支付信息
     *
     * @param paymentVO 操作数据
     * @return java.lang.String
     * @author Idenn
     * @date 3/7/2024 8:42 PM
     */
    @Override
    public String update(PaymentVO paymentVO) {
        String msg = "更新成功!";
        PaymentPO paymentPO = new PaymentPO();
        BeanUtils.copyProperties(paymentVO, paymentPO);
        if (paymentMapper.updateById(paymentPO) > 0) {
            return msg;
        } else {
            throw new BaseException("支付模块更新失败！");
        }
    }

    /**
     * 根据ID真实删除
     *
     * @param paymentVO 要删除的id
     * @return java.lang.String
     * @author Idenn
     * @date 3/13/2024 1:54 PM
     */
    @Override
    public String trueDeleteById(PaymentVO paymentVO) {
        String msg = "真实删除成功！";
        PaymentPO paymentPO = new PaymentPO();
        BeanUtils.copyProperties(paymentVO, paymentPO);
        if (trueDelete(paymentPO) > 0) {
            return msg;
        } else {
            throw new BaseException("支付模块真实删除失败！");
        }
    }

    @Override
    @Transactional
    public String createPaymentByOrder(String orderId) {
        System.out.println("事务id---------------------->" + RootContext.getXID());
        String msg = "创建支付信息成功";
        PaymentPO paymentPO = new PaymentPO();
        paymentPO.setOrderId(orderId);
        paymentPO.setUserId(UserHolder.getUser().getObjectId());
        paymentMapper.insert(paymentPO);
        throw new BaseException("测试错误");
        // return msg;
    }


}
