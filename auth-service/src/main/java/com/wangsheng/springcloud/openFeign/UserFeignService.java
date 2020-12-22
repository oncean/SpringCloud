package com.wangsheng.springcloud.openFeign;

import com.wangsheng.springcloud.common.model.usercenter.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.wangsheng.springcloud.common.model.result.Result;

@FeignClient("user-center")
public interface UserFeignService {

    @GetMapping("/user/{id}")
    Result<User> getUserDetail(@PathVariable(value = "id") Object id);
}
