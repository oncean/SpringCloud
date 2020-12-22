package com.wangsheng.springcloud.service;

import com.wangsheng.springcloud.common.model.result.Result;
import com.wangsheng.springcloud.common.model.result.ResultCode;
import com.wangsheng.springcloud.common.model.usercenter.Role;
import com.wangsheng.springcloud.common.model.usercenter.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final static Role ADMIN_ROLE = Role.builder().id(1).name("admin").build();
    private final static Role USER_ROLE = Role.builder().id(2).name("user").build();

    private final static List<User> DEFAULT_USERS = Arrays.asList(
            User.builder().id((long) 1).username("admin").password("admin").roles(Arrays.asList("admin","user")).build(),
            User.builder().id((long) 1).username("user1").password("user1").roles(Arrays.asList("user")).build()
    );

    public Result detail(String id) {
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
