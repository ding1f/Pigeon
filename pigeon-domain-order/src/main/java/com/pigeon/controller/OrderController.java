package com.pigeon.controller;

import com.pigeon.entity.order.vo.OrderCreateVO;
import com.pigeon.entity.order.vo.OrderQueryVO;
import com.pigeon.entity.order.vo.OrderResultVO;
import com.pigeon.entity.order.vo.OrderVO;
import com.pigeon.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单Controller
 *
 * @author Idenn
 * @date 4/2/2024 9:06 PM
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 分页查询
     *
     * @param orderQueryVO
     * @return java.util.List<com.pigeon.entity.order.vo.OrderResultVO>
     * @author Idenn
     * @date 3/26/2024 10:05 PM
     */
    @GetMapping("/getOrderByPage")
    public List<OrderResultVO> getOrderByPage(@RequestBody OrderQueryVO orderQueryVO) {
        return orderService.getOrderByPage(orderQueryVO);
    }

    /**
     * 新增菜单
     *
     * @param orderVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:19 PM
     */
    @PostMapping("/create")
    public String create(@RequestBody OrderCreateVO orderVO) {
        return orderService.create(orderVO);
    }

    /**
     * 更新菜单
     *
     * @param orderVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:20 PM
     */
    @PostMapping("/update")
    public String update(@RequestBody OrderVO orderVO) {
        return orderService.update(orderVO);
    }

    /**
     * 删除菜单
     *
     * @param orderVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @DeleteMapping("/deleteById")
    public String deleteById(@RequestBody OrderVO orderVO) {
        String id = orderVO.getObjectId();
        return orderService.deleteById(id);
    }

    /**
     * 真实删除
     *
     * @param orderVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @DeleteMapping("/trueDeleteById")
    public String trueDeleteById(@RequestBody OrderVO orderVO) {
        return orderService.trueDeleteById(orderVO);
    }

    /*
     * 测试rabbitmq组件
     */
    @PostMapping("/testOrder")
    public String testOrder(@RequestBody OrderVO orderVO) {
        return orderService.testOrder(orderVO);
    }

    /*
     * 测试分布式事务
     */
    @PostMapping("/createOrderAndPayment")
    public String createOrderAndPayment(@RequestBody OrderVO orderVO) {
        return orderService.createOrderAndPayment(orderVO);
    }

}
