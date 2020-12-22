package com.wangsheng.springcloud.dubbo;

import com.wangsheng.springcloud.common.model.result.Result;
import com.wangsheng.springcloud.service.DUserService;
import com.wangsheng.springcloud.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "1.0.0")
public class DUserServiceImpl implements DUserService {
    @Autowired
    private UserService userService;

    @Override
    public Result detail(String id) {
        return userService.detail(id);
    }
}
