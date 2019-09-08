package com.neuedu.controller.backend;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.User;
import com.neuedu.service.IProductService;
import com.neuedu.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/manage/product/")
public class ProductController {
    @Autowired
    IProductService productService;
    /**
     *商品添加&更新
     */
    @RequestMapping(value="save.do")
    public ServerResponse addOrUpdate(Product product, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN, "未登录");
        }
        int role = user.getRole();
        if (role == RoleEnum.ROLE_USER.getRole()) {
            return ServerResponse.serverResponseByError(ResponseCode.ERROR, "权限不足");
        }
      return productService.addOrUpdate(product);
    }

    /**
     * 搜索商品
     */
    @RequestMapping(value = "search.do")
    public String search(@RequestParam(name = "productName",required = false)String productName,
                                 @RequestParam(name = "productId",required = false)String productId
                                 ){
        List<Product> products = productService.search(productName,productId);
        return products.toString();
    }

    /**
     * 商品详情
     */

    @RequestMapping(value = "/{productId}")
    public ServerResponse detail(@PathVariable("productId")Integer productId,
                                 HttpSession session){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录");
        }

        int role=user.getRole();
        if (role==RoleEnum.ROLE_USER.getRole()){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
        }
        return productService.detail(productId);
    }
}
