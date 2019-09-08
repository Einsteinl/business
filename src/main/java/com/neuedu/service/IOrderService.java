package com.neuedu.service;


import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.OrderItem;

import java.util.List;

public interface IOrderService {
    /*创建订单
    * */
    public ServerResponse createOrder(Integer userId, Integer shippingId);

    /**
     * 根据userId查询用户的所有订单
     */
    public List<OrderItem> findOrdersByUserId(String userId);

    public Integer addOrderItemList(List<OrderItem> orderItems);
}
