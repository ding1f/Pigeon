package com.pigeon.service;

import com.pigeon.entity.order.po.OrderPO;
import com.pigeon.entity.order.vo.OrderCreateVO;
import com.pigeon.entity.order.vo.OrderQueryVO;
import com.pigeon.entity.order.vo.OrderResultVO;
import com.pigeon.entity.order.vo.OrderVO;

import java.util.List;

/**
 * 订单服务层
 *
 * @author Idenn
 * @date 4/2/2024 9:20 PM
 */
public interface OrderService extends BaseService<OrderPO> {
    List<OrderResultVO> getOrderByPage(OrderQueryVO orderQueryVO);

    String create(OrderCreateVO orderVO);

    String update(OrderVO orderVO);

    String deleteById(String id);

    String trueDeleteById(OrderVO orderVO);

    String testOrder(OrderVO orderVO);

    String createOrderAndPayment(OrderVO orderVO);
}