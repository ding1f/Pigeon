package com.pigeon.controller;

import com.pigeon.entity.payment.vo.PaymentQueryVO;
import com.pigeon.entity.payment.vo.PaymentResultVO;
import com.pigeon.entity.payment.vo.PaymentVO;
import com.pigeon.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 支付模块的controller
 *
 * @author Idenn
 * @date 3/5/2024 8:31 PM
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    /**
     * 分页查询
     *
     * @param paymentQueryVO 查询的参数
     * @return java.util.List<com.pigeon.entity.vo.PaymentResultVO>
     * @author Idenn
     * @date 3/11/2024 9:34 PM
     */
    @GetMapping("/getPaymentByPage")
    public List<PaymentResultVO> getPaymentByPage(@RequestBody PaymentQueryVO paymentQueryVO) {
        return paymentService.getPaymentByPage(paymentQueryVO);
    }

    /**
     * 根据id获取支付记录
     *
     * @param paymentVO 支付记录id
     * @return com.pigeon.response.CommonResponse
     * @author Idenn
     * @date 3/7/2024 7:49 PM
     */
    @GetMapping("/getById")
    public PaymentVO getById(@RequestBody PaymentVO paymentVO) {
        String id = paymentVO.getObjectId();
        return paymentService.getById(id);
    }

    /**
     * 插入一条支付记录
     *
     * @param paymentVO 支付记录参数
     * @return int
     * @author Idenn
     * @date 3/7/2024 4:56 PM
     */
    @PostMapping("/create")
    public String create(@RequestBody PaymentVO paymentVO) {
        return paymentService.create(paymentVO);
    }

    /**
     * 通过id删除支付记录
     *
     * @param paymentVO 要删除的id
     * @return com.pigeon.response.CommonResponse
     * @author Idenn
     * @date 3/7/2024 7:58 PM
     */
    @DeleteMapping("/deleteById")
    public String deleteById(@RequestBody PaymentVO paymentVO) {
        String id = paymentVO.getObjectId();
        return paymentService.deleteById(id);
    }

    /**
     * 更新支付信息
     *
     * @param paymentVO 要删除的id
     * @return com.pigeon.response.CommonResponse
     * @author Idenn
     * @date 3/7/2024 8:44 PM
     */
    @PostMapping("/update")
    public String update(@RequestBody PaymentVO paymentVO) {
        return paymentService.update(paymentVO);
    }

    /**
     * 根据ID真实删除
     *
     * @param paymentVO 要删除的id
     * @return java.lang.String
     * @author Idenn
     * @date 3/13/2024 1:53 PM
     */
    @DeleteMapping("/trueDeleteById")
    public String trueDeleteById(@RequestBody PaymentVO paymentVO) {
        return paymentService.trueDeleteById(paymentVO);
    }

    /*
     * 测试分布式事务
     */
    @PostMapping("/createPaymentByOrder")
    public String createPaymentByOrder(String orderId) {
        return paymentService.createPaymentByOrder(orderId);
    }
}
