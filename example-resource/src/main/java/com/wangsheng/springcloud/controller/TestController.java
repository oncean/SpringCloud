package com.wangsheng.springcloud.controller;

import com.wangsheng.springcloud.common.model.result.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("/test")
    public Result test(Principal user){
        return Result.success("test");
    }
    @GetMapping("user")
    public Result testUser(){
        return Result.success("user");
    }


    @GetMapping("admin")
    @PreAuthorize("hasRole('ROLE_admin')")
    public Result testAdmin(){
        return Result.success("admin");
    }
}
