package com.neuedu.service.impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.RoleEnum;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserMapper;
import com.neuedu.pojo.User;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
   private UserMapper userMapper;

    @Override
    public User register(User user) {

     //step1:参数校验
     if(user==null){
        return null;
     }
     //step2: 判断用户名是否存在
     int result=userMapper.isexistsusername(user.getUsername());
     if(result>0){//用户名已存在
         return null;
     }
     //step3:判断邮箱是否存在
        int resultemail=userMapper.isexistsemail(user.getEmail());
        if(resultemail>0){//邮箱已存在
            return null;
        }
     //step4: MD5密码加密，设置用户角色 ADMIN ---XXXXXXXXXXXXX
       //user.setPassword(getMD5Code(user.getPassword()));
        //设置角色为普通用户
        user.setRole(RoleEnum.ROLE_USER.getRole());
     //step5: 注册
       int insertResult= userMapper.insert(user);
       if(insertResult<=0){
           return null;
       }
     //step6：返回
      return user;
    }

    /**
     *密保问题
     * @param username
     * @return
     */
    @Override
    public ServerResponse forget_get_question(String username) {
        //step1：参数非空校验

        if(username==null||username.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"用户名不能为空");
        }

        //step2：根据用户名查询问题
        String question=userMapper.forget_get_question(username);
        if (question==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"没有查询到密保问题");
        }

        //step3:返回结果
        return ServerResponse.serverResponseBySuccess(question);
    }

    /**
     * 提交答案
     * @param username
     * @param question
     * @param answer
     * @return
     */

    @Override
    public ServerResponse forget_check_answer(String username,String question,String answer) {
        //step1：参数非空校验
        if(username==null||username.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        }
        if(question==null||question.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密保问题不能为空");
        }
        if(answer==null||answer.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案不能为空");
        }

        //step2：校验答案
        int result=userMapper.forget_check_answer(username,question,answer);
        if (result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案错误");
        }

        //step3:返回结果
        return ServerResponse.serverResponseBySuccess();
    }

    /**
     * 修改密码
     * @param username
     * @param newpassword
     * @param forgettoken
     * @return
     */
    @Override
    public ServerResponse forget_reset_password(String username, String newpassword, String forgettoken) {
        //step1：参数非空校验
        if(username==null||username.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        }
        if(newpassword==null||newpassword.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"新密码不能为空");
        }
        if(forgettoken==null||forgettoken.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"token不能为空");
        }

        int result=userMapper.forget_reset_password(username,newpassword);
        if (result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码修改失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse update_information(User user) {

        //step1:参数校验
        if(user==null){
            return  ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空！");
        }
      int result = userMapper.updateUserByActivate(user);
        if (result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public List<User> findAllUser() {
        return userMapper.finAllUser();
    }

    @Override
    public ServerResponse deleteUser(String username) {
        userMapper.deleteUser(username);
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public User findUserByUserId(String userId) {
        return userMapper.findUserByUserId(Integer.valueOf(userId));
    }



    /**
     *
     * @param username password
     *                 type1:普通用户 0：管理员
     * @param password
     * @param type
     * @return
     */
    @Override
    public User login(String username,String password,int type){
        //step1:参数校验
        if(username==null||username.equals("")){
            return  null;
        }
        if(password==null||password.equals("")){
            return  null;
        }
        //step2：判断用户名是否存在
       int result=userMapper.isexistsusername(username);
        if (result<=0){//用户名不存在
            return null;
        }
        //step3：密码加密，用户名存在password=MD5Utils.getMD5Code(password);
        //step4:登录
        User user=userMapper.findUserByUsernameAndPassword(username,password);
        if (user==null){//密码错误
            return null;
        }

        if (type == 0) {//管理员
            if (user.getRole()==RoleEnum.ROLE_USER.getRole()){//没有管理员权限
                return null;
            }

        }
        return user;
    }
    @Override
    public User findUserByUserName(String userName) {
        return userMapper.findUserByUserName(userName);
    }
}
