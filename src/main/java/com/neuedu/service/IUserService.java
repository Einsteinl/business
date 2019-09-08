package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IUserService {

    /**
     * 注册接口
     * @param user
     * @return ServerResponse
     * */

    public User register(User user);

    /**
     * 登录接口
     * @param username password
     *                 type1:普通用户 0：管理员
     * @return ServerResponse
     * */

    public User login(String username,String password,int type);



    /**
     * 根据username获取密保问题
     */


    public ServerResponse forget_get_question(@PathVariable("username") String username);


    /**
     * 提交答案
     */


    public ServerResponse forget_check_answer(String username,String question,String answer);


    /**
     * 修改密码
     */


    public ServerResponse forget_reset_password(String username,String newpassword,String forgettoken);

    /**
     * 修改密码
     */
    public ServerResponse update_information(User user);

    //查询所有用户
    public List<User> findAllUser();

    //删除用户
    public ServerResponse deleteUser(String username);

    /**
     * 根据userID获取user对象
     */
    public User findUserByUserId(String userId);

    /**
     * 根据userName获取user对象
     */
    public User findUserByUserName(String userName);
}
