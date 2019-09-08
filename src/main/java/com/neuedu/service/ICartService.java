package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.CartDetail;

import java.util.List;

public interface ICartService {
    /**
     * 添加商品到购物车
     */
    public ServerResponse addProductToCart(Integer userId,Integer productId,Integer count);
  /*
  * 根据用户id查看用户已选中的商品
  * */
  public ServerResponse<List<Cart>> findCartsByUseridAndChecked(Integer userId);


    /**
     * 批量删除购物车商品
     */
    public ServerResponse deleteBatch(List<Cart> cartList);

    /**
     * 根据username获取用户的购物车
     */

    public List<CartDetail> findCartByUserId(String username);

}
