package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.OrderStatusEnum;
import com.neuedu.common.ProductStatusEnum;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.OrderItemMapper;
import com.neuedu.dao.OrderMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Order;
import com.neuedu.pojo.OrderItem;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.utils.DateUtils;
import com.neuedu.vo.OrderItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    ICartService cartService;
    @Autowired
    IProductService productService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
       //step1；参数的非空校验
        if (shippingId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"收货地址");
        }
        //判断shippingid是否存在

        //step2：根据userID查看用户购物车中已选择的商品List<Cart>
        ServerResponse<List<Cart>> serverResponse=cartService.findCartsByUseridAndChecked(userId);
        List<Cart> cartList=serverResponse.getData();
        if (cartList==null || cartList.size()==0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车为空或者未选中购物");
        }
        //step3:LiST<Cart>-->List<OrderItem>
        ServerResponse orderItems_serverResponse=getCarOrderItem(userId,cartList);
        if(!orderItems_serverResponse.isSuccess()){
            return orderItems_serverResponse;
        }
        List<OrderItem>orderItemList=(List<OrderItem>) orderItems_serverResponse.getData();
        //step4:创建Order实体类并保存到DB-order
        ServerResponse<Order> order_ServerResponse=createOrder(userId,shippingId,orderItemList);
        if (!order_ServerResponse.isSuccess()){
            return order_ServerResponse;
        }
        Order order=order_ServerResponse.getData();
        //保存订单明细
       ServerResponse serverResponse1=saveOrderItems(orderItemList,order);
           if (!serverResponse1.isSuccess()){
               return serverResponse1;
           }
        //step6:加库存
        reduceProductStock(orderItemList);
        //step7:清空购物车中下单的商品
         ServerResponse cart_serverResponse=cartService.deleteBatch(cartList);
        if (!cart_serverResponse.isSuccess()){
            return order_ServerResponse;
        }
         //step8：返回orderVO

        return null;
    }

    @Override
    public List<OrderItem> findOrdersByUserId(String userId) {
        return orderItemMapper.selectByUserId(Integer.valueOf(userId));
    }

    @Override
    public Integer addOrderItemList(List<OrderItem> orderItems) {
        int count = 0;
        for(OrderItem orderItem:orderItems){
            int insert = orderItemMapper.insert(orderItem);
            if(insert > 0){
                count ++;
            }
        }
        return count;
    }

    //扣库存
    private ServerResponse reduceProductStock(List<OrderItem> orderItemList){
            for (OrderItem orderItem:orderItemList){
                Integer productId=orderItem.getProductId();
                ServerResponse<Product>serverResponse=productService.findProductById(productId);
                Product product=serverResponse.getData();
                int stock=product.getStock()-orderItem.getQuantity();
                ServerResponse serverResponse1=productService.reduceStock(productId,stock);
                if (!serverResponse1.isSuccess()){
                    return serverResponse1;
                }
            }
            return ServerResponse.serverResponseBySuccess();
    }

    //step5:保存List<OrderItemVO>--order-item
    private ServerResponse saveOrderItems(List<OrderItem>orderItemList,Order order){
        for (OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //insert into () values(),(),()...
        int result=orderItemMapper.insertBatch(orderItemList);
        if (result!=orderItemList.size()){
            //有些订单明细没有插入成功
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"订单明细保存失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    private ServerResponse createOrder(Integer userId, Integer shippingid, List<OrderItem> orderItemList){
       Order order=new Order();
       order.setUserId(userId);
       order.setShippingId(shippingid);
       order.setOrderNo(generatorOrderNo());
       order.setPayment(getOrderTotalPrice(orderItemList));
       order.setPaymentType(1);
       order.setPostage(0);
       order.setStatus(OrderStatusEnum.ORDER_NO_PAY.getStatus());

      int result=orderMapper.insert(order);
       if (result<=0){
           return ServerResponse.serverResponseByError(ResponseCode.ERROR,"订单保存失败");
       }
        return ServerResponse.serverResponseBySuccess(order);
    }

    /**
     * 生成订单号
     */
    private Long generatorOrderNo(){
        return System.currentTimeMillis()+new Random().nextInt(100);
    }

    /**
     * 计算订单的总价格
     */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){
            BigDecimal orderTotalPrice=new BigDecimal("0");
             for (OrderItem orderItem:orderItems){
                 orderTotalPrice=BigDecimalUtils.add(orderItem.getTotalPrice().doubleValue(),orderTotalPrice.doubleValue());
             }
             return orderTotalPrice;
    }
    private ServerResponse getCarOrderItem(Integer userId,List<Cart> cartList){
        List<OrderItem> orderItemList= Lists.newArrayList();
        for (Cart cart:cartList){
            OrderItem orderItem=new OrderItem();
            orderItem.setUserId(userId);
            ServerResponse<Product> serverResponse=productService.findProductByProductId(cart.getProductId());
            if (!serverResponse.isSuccess()){
                return serverResponse;
            }
             Product product=serverResponse.getData();
            if (product==null){
                return ServerResponse.serverResponseByError("id为"+cart.getProductId()+"的商品不存在");
            }
            if (product.getStatus()!= ProductStatusEnum.PRODUCT_SALE.getStatus()){
                return ServerResponse.serverResponseByError("id为"+product.getId()+"商品已经下架");
            }
            if (product.getStock()< cart.getQuantity()){
                return ServerResponse.serverResponseByError("id为"+product.getId()+"的商品库存不足");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));

            orderItemList.add(orderItem);
        }
        return ServerResponse.serverResponseBySuccess(orderItemList);
    }


    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO=new OrderItemVO();
        if (orderItem!=null){
            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateToStr(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        }
        return orderItemVO;
    }
}
