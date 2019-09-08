package com.neuedu.controller.front;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.OrderItem;
import com.neuedu.pojo.User;
import com.neuedu.service.IOrderService;
import com.neuedu.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    IOrderService orderService;
    /*创建订单接口
    * */
    @RequestMapping("{shippingiId}")
    public ServerResponse createOrder(@PathVariable("shippingId")Integer shippingId,
                                              HttpSession session){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }
       return orderService.createOrder(user.getId(),shippingId);
    }

    /**
     * 查询所有订单
     */
    @GetMapping(path = "user/{userId}")
    public Iterable<OrderItem> findOrdersByUserId(@PathVariable String userId){
        return orderService.findOrdersByUserId(userId);
    }

    /**
     * 添加订单List
     */
    @PostMapping("add")
    public String addOrderItems(@RequestBody List<OrderItem> orderItemList){
        return orderService.addOrderItemList(orderItemList).toString();
    }
}
