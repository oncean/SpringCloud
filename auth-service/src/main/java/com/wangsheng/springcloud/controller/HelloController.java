package com.wangsheng.springcloud.controller;

import com.wangsheng.springcloud.openFeign.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(Principal principal){
        return "hello "+principal.getName();
    }

}
