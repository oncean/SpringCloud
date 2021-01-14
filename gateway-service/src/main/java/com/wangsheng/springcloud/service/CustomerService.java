package com.wangsheng.springcloud.service;

import com.wangsheng.springcloud.common.redis.RedisTool;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public void setResult(){
        RedisTool.setRedis();
    }

    public void getResult(){
        RedisTool.getRedis();
    }
}
