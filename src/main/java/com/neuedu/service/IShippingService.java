package com.neuedu.service;


import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IShippingService {
    public ServerResponse add(Shipping shipping);
}
