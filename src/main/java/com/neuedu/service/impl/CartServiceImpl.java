package com.neuedu.service.impl;

import com.neuedu.common.CheckEnum;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.CartDetail;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import com.neuedu.service.IProductService;
import com.neuedu.service.IUserService;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    IProductService productService;
    @Autowired
    CartMapper cartMapper;

    @Autowired
    IUserService userService;

    @Override
    public ServerResponse addProductToCart(Integer userId, Integer productId, Integer count) {
        //step1:参数非空判断
        if (productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id必传");
        }
        if (count==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品数量不能为0");
        }
        //step2:判断商品是否存在
        ServerResponse<Product> serverResponse=productService.findProductById(productId);
        if (!serverResponse.isSuccess()){//商品不存在
            return ServerResponse.serverResponseByError(serverResponse.getStatus(),serverResponse.getMsg());
        }else{
           Product product=serverResponse.getData();
           if (product.getStock()<=0){
               return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品已售空");
           }
        }


        //step3：判断商品是否在购物车中
       Cart cart=cartMapper.findCartByUseridAndProductId(userId,productId);
        if (cart==null){
            //添加
            Cart newCart=new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(count);
            newCart.setChecked(CheckEnum.CART_PRODUCT_CHECK.getCheck());
            int result=cartMapper.insert(newCart);
            if (result<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败");
            }
        }else{
            //更新商品在购物车中的数量

            cart.setQuantity(cart.getQuantity()+count);
            int result=cartMapper.updateByPrimaryKey(cart);
            if (result<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新失败");
            }
        }
        //step4：封装购物车对象



        //step5：返回CartVO


        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse<List<Cart>> findCartsByUseridAndChecked(Integer userId) {

       List<Cart> cartList=cartMapper.findCartsByUseridAndChecked(userId);
        return ServerResponse.serverResponseBySuccess(cartList);
    }

    @Override
    public ServerResponse deleteBatch(List<Cart> cartList) {
        if (cartList==null || cartList.size()==0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"要删除的购物车商品");
        }
        int result=cartMapper.deleteBatch(cartList);
        if (result!=cartList.size()){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车清空失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public List<CartDetail> findCartByUserId(String username) {
        User user_basic = userService.findUserByUserName(username);
        if(user_basic == null){
            return new ArrayList<>();
        }
        List<Cart> cartsByUserName = cartMapper.findCartsByUserid(user_basic.getId());
        return turnCartDetails(cartsByUserName);
    }

    private List<CartDetail> turnCartDetails(List<Cart> cartsByUserName) {
        List<CartDetail> cartDetails = new ArrayList<>();
        for(Cart cart:cartsByUserName){
            CartDetail cartDetail = new CartDetail();
            Product product = productService.findProductById(cart.getProductId().toString());
            User user = userService.findUserByUserId(cart.getUserId().toString());
            cartDetail.setCartId(cart.getId().toString());
            cartDetail.setQuantity(cart.getQuantity());
            cartDetail.setUsername(user.getUsername());
            cartDetail.setProduct(product);
            cartDetails.add(cartDetail);
        }
        return cartDetails;
    }

    private CartVO getCarVO(Integer userId){
        CartVO cartVO=new CartVO();
        //step1:根据userID查询该用户的购物信息-》List<Cart>
        List<Cart> cartList=cartMapper.findCartsByUserid(userId);
        if (cartList==null || cartList.size()==0){
              return cartVO;
        }

        //step2:List<Cart>-->List<CartProductVO>
         for (Cart cart:cartList){
             //Cart-->CartProductVO
             CartProductVO cartProductVO=new CartProductVO();
             cartProductVO.setId(cart.getId());
             cartProductVO.setUserId(userId);
             cartProductVO.setProductId(cart.getProductId());
             cartProductVO.setQuantity(cart.getQuantity());
             ServerResponse<Product>serverResponse=productService.findProductById(cart.getProductId());
             if (serverResponse.isSuccess()){
                 Product product=serverResponse.getData();
                 cartProductVO.setProductName(product.getName());
                 cartProductVO.setProductPrice(product.getPrice());
                 cartProductVO.setProductStatus(product.getStatus());
             }


         }
        //step3：计算购物车总的价格

        //step4：判断是否全选

        //step5：构建cartVO

        return cartVO;

    }
}
