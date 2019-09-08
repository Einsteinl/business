package com.neuedu.controller;

import com.neuedu.dao.CartMapper;
import com.neuedu.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
public class HelloController {
    @GetMapping(value = "/upload")
    public String upload(){
        return "upload";
    }
    @Autowired
    CartMapper cartMapper;
    @RequestMapping("/login/{username}/{password}")
    public List<Cart> testrestful(@PathVariable("username") String username, @PathVariable("password")String password){

        List<Cart> userList= cartMapper.selectAll();
        return userList;

    }

    @RequestMapping("/testlog")
    public List<Cart> testlog(){

        List<Cart> userList= cartMapper.selectAll();
        int a=3/0;
        return userList;

    }

}
