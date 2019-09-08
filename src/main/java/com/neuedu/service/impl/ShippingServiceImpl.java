package com.neuedu.service.impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Shipping shipping) {

        //step1:参数非空判断
        if (shipping==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数必传");
        }
        Integer shippingid=shipping.getId();
        if (shippingid==null){
            //添加
          int result= shippingMapper.insert(shipping);
          if (result<=0){
              return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加地址失败");
          }else{
              return ServerResponse.serverResponseBySuccess(shipping.getId());
          }
        }else{
            //更新
        }
        return null;
    }
}
