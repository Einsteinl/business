package com.neuedu.controller.front;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.CartDetail;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.User;
import com.neuedu.service.ICartService;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    ICartService cartService;

    @Autowired
    IUserService userService;

    @Autowired
    CartMapper cartMapper;



    /**
     * 添加商品到购物车
     */
    @RequestMapping("add")
    @ResponseBody
    public CartDetail addCart(@RequestBody CartDetail cartDetail){
        User user = userService.findUserByUserName(cartDetail.getUsername());
        Product product = cartDetail.getProduct();
        if (user == null){
            return null;
        }

        ServerResponse serverResponse = cartService.addProductToCart(user.getId(), product.getId(), cartDetail.getQuantity());
        if(serverResponse.isSuccess()){
            Cart cart=cartMapper.findCartByUseridAndProductId(user.getId(), product.getId());
            cartDetail.setCartId(cart.getId().toString());
            cartDetail.setQuantity(cart.getQuantity());
            return cartDetail;
        }
        return null;

    }

    /**
     * 根据userID查看用户的购物车
     */
    @GetMapping(path = "/username/{username}")
    public @ResponseBody Iterable<CartDetail> getAllCartItemsByUserId(@PathVariable String username){
        System.out.println("Fetching all cart items");

        return cartService.findCartByUserId(username);
    }

    /**
     * 根据userID删除用户的购物车
     */
    @DeleteMapping(path = "/username/{username}")
    public @ResponseBody String deleteCartsByUserId(@PathVariable String username){
        System.out.println("Fetching all cart items");
        User user = userService.findUserByUserName(username);
        if(user == null){
            return "0";
        }
        List<Cart> carts = cartMapper.findCartsByUserid(user.getId());
        Integer count = 0;
        for(Cart cart:carts){
            int i = cartMapper.deleteByPrimaryKey(Integer.valueOf(cart.getId()));
            if(i > 0){
                count += i;
            }
        }
        return count.toString();
    }

    @DeleteMapping("delete/{cid}")
    public String deleteCartItem(@PathVariable String cid){

        int i = cartMapper.deleteByPrimaryKey(Integer.valueOf(cid));
        if(i < 0){
            return "";
        }else {
            return "1";
        }
    }

}
