package com.pigeon.dao.order;

import com.pigeon.dao.CustomMapper;
import com.pigeon.entity.order.po.OrderPO;
import com.pigeon.entity.order.vo.OrderQueryVO;
import com.pigeon.entity.order.vo.OrderResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单模块dao
 *
 * @author Idenn
 * @date 4/2/2024 9:06 PM
 */
@Mapper
public interface OrderMapper extends CustomMapper<OrderPO> {

    /*
     * 分页查询菜单
     */
    List<OrderResultVO> getOrderByPage(OrderQueryVO orderQueryVO);
}
