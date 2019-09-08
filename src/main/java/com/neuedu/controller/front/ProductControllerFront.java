package com.neuedu.controller.front;

import com.neuedu.pojo.Product;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductControllerFront {
    @Autowired
    IProductService productService;
    /**
     *查询所有商品
     */
    @RequestMapping(value="/findAll")
    public List<Product> findAll() {

      return productService.findAll();
    }

    /**
     * 查询类别下的所有的商品
     */
    @RequestMapping(value = "/findProductByCategoryID/{categoryID}")
    public List<Product> findProductByCategoryID(@PathVariable String categoryID){
        return productService.findProductByCategoryID(categoryID);
    }

    /**
     * 获取商品详情
     */
    @GetMapping(path = "/{pid}")
    public Product getProduct(@PathVariable String pid){
        System.out.println("Fetching product "+pid);
        return productService.findProductById(pid);
    }


}
