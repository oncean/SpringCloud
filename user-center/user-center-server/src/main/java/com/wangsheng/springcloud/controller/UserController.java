package com.wangsheng.springcloud.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.wangsheng.springcloud.common.model.result.Result;
import com.wangsheng.springcloud.common.model.result.ResultCode;
import com.wangsheng.springcloud.common.model.usercenter.Role;
import com.wangsheng.springcloud.common.model.usercenter.User;
import com.wangsheng.springcloud.stream.kafka.MSChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private MSChannel msChannel;

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

    @GetMapping("loginTest")
    public Result loginTest(){
        try{
            msChannel.login_producer().send(
                    MessageBuilder.withPayload(
                            JSONUtil.toJsonStr(Result.success("login success"+ DateTime.now()))
                    ).setHeader()
                            .build()
            );
        }catch (Exception e){
            log.error("消息发送失败，原因",e);
        }
        return Result.success("login success");
    }
}
