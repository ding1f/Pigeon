package com.pigeon.service.impl;

import com.pigeon.context.UserHolder;
import com.pigeon.dao.order.OrderMapper;
import com.pigeon.entity.order.po.OrderPO;
import com.pigeon.entity.order.vo.OrderCreateVO;
import com.pigeon.entity.order.vo.OrderQueryVO;
import com.pigeon.entity.order.vo.OrderResultVO;
import com.pigeon.entity.order.vo.OrderVO;
import com.pigeon.enums.ResultCode;
import com.pigeon.exception.BaseException;
import com.pigeon.feign.PaymentClient;
import com.pigeon.manager.RabbitMQManager;
import com.pigeon.response.CommonResponse;
import com.pigeon.service.OrderService;
import com.pigeon.utils.SnowflakeIdWorker;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 订单服务实现类
 *
 * @author Idenn
 * @date 4/2/2024 9:20 PM
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderPO> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RabbitMQManager rabbitMQManager;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private PaymentClient paymentClient;

    /**
     * 分页查询
     *
     * @param orderQueryVO 分页查询参数
     * @return java.util.List<com.pigeon.entity.order.vo.OrderResultVO>
     * @author Idenn
     * @date 3/26/2024 10:05 PM
     */
    @Override
    public List<OrderResultVO> getOrderByPage(OrderQueryVO orderQueryVO) {
        return orderMapper.getOrderByPage(orderQueryVO);
    }

    /**
     * 新增菜单
     *
     * @param orderVO 菜单参数
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:19 PM
     */
    @Override
    public String create(OrderCreateVO orderVO) {
        String msg = "插入成功！";
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        if (orderMapper.insert(orderPO) == 1) {
            return msg;
        } else {
            throw new BaseException("菜单模块插入失败！");
        }
    }

    /**
     * 更新菜单
     *
     * @param orderVO 更新菜单的参数
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:20 PM
     */
    @Override
    public String update(OrderVO orderVO) {
        String msg = "更新成功!";
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        if (orderMapper.updateById(orderPO) > 0) {
            return msg;
        } else {
            throw new BaseException("菜单模块更新失败！");
        }
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @Override
    public String deleteById(String id) {
        String msg = "删除成功！";
        if (orderMapper.deleteById(id) > 0) {
            return msg;
        } else {
            throw new BaseException("菜单模块删除失败！");
        }
    }

    /**
     * 真实删除
     *
     * @param orderVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @Override
    public String trueDeleteById(OrderVO orderVO) {
        String msg = "真实删除成功！";
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        if (trueDelete(orderPO) > 0) {
            return msg;
        } else {
            throw new BaseException("菜单模块真实删除失败！");
        }
    }

    @Override
    public String testOrder(OrderVO orderVO) {
        String msg = "测试Order成功";
        rabbitMQManager.send("pigeon.order.exchange", "pigeon.order.orderRoutingKey", orderVO, UserHolder.getUser());
        return msg;
    }

    @Override
    @GlobalTransactional
    @Transactional
    public String createOrderAndPayment(OrderVO orderVO) {
        System.out.println("事务id---------------------->" + RootContext.getXID());
        String msg = "订单生成成功！";
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        String orderId = snowflakeIdWorker.nextId();
        orderPO.setObjectId(orderId);
        orderPO.setUserId(UserHolder.getUser().getObjectId());
        if (orderMapper.insert(orderPO) != 1) {
            throw new BaseException("订单生成失败！");
        }
        CommonResponse paymentByOrder = paymentClient.createPaymentByOrder(orderId);
        System.out.println(paymentByOrder);
        if (paymentByOrder.getCode() != ResultCode.SUCCESS.getCode()) {
            throw new BaseException(paymentByOrder.getMessage());
        }
        return msg;
    }


}
