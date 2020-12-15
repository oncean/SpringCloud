package com.wangsheng.springcloud.security;

import com.wangsheng.springcloud.model.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public Result test(){
        return Result.success();
    }
}
