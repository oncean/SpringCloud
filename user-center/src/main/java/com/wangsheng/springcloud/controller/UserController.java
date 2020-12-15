package com.wangsheng.springcloud.controller;

import com.wangsheng.springcloud.model.result.Result;
import com.wangsheng.springcloud.model.result.ResultCode;
import com.wangsheng.springcloud.model.usercenter.Role;
import com.wangsheng.springcloud.model.usercenter.User;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private final static Role ADMIN_ROLE = Role.builder().id(1).name("admin").build();
    private final static Role USER_ROLE = Role.builder().id(2).name("user").build();

    private final static List<User> DEFAULT_USERS = Arrays.asList(
            User.builder().id((long) 1).username("admin").password("admin").roles(Arrays.asList("admin","user")).build(),
            User.builder().id((long) 1).username("user1").password("user1").roles(Arrays.asList("user")).build()
    );

    @GetMapping("{id}")
    public Result detail(@PathVariable Object id) {
        User user = null;
        for (User item :
                DEFAULT_USERS) {
            if(item.getUsername().equals(id)){
                user = item;
            }
        }
        if(user != null){
            return Result.success(user);
        }else{
            return Result.failed(ResultCode.USER_NOT_EXIST);
        }
    }
}
