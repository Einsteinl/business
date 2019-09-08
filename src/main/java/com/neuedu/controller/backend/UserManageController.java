package com.neuedu.controller.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/manage/")
public class UserManageController {

    @Autowired
    IUserService userService;


    @RequestMapping("findAllUser")
    public String findAllUser(){
        List<User> allUser = userService.findAllUser();
        return allUser.toString();
    }

    @RequestMapping("deleteUser")
    public ServerResponse deleteUser(@RequestParam("username")String username){
        return userService.deleteUser(username);
    }
}
