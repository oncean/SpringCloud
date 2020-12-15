package com.wangsheng.springcloud.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的用户校验方式
 */
@Service
@Slf4j
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {
    private final static Map<String,String> users = new HashMap<String,String>(){{
        put("wangsheng1",encodePassword("1")+",admin");
        put("wangsheng2",encodePassword("1")+",user");
    }};

    private static String encodePassword(String str){
        //BCryptPasswordEncoder每次都使用随机盐（salt），但是会把salt保存在结果中，
        //再使用BCryptPasswordEncoder.match方法时则会取出salt对比较值进行同样算法的加密和对比
        return new BCryptPasswordEncoder().encode(str);
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("find user by name " + username);
        String user = users.get(username);
        if(null == user){
            return Mono.empty();
        }
        String[] result = user.split(",");
        String password = result[0];
        String role = result[1];
        UserDetails userDetails = User.builder()
                .username(username)
                .password(password)
                .roles(new String[]{role}).build();
        return null == userDetails? Mono.empty():Mono.just(User.withUserDetails(userDetails).build());
    }

    // 使用BCryptPassword加密方式
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    //自定义的 密码解码方式
    // @Bean
    public PasswordEncoder encoderDefind(){
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        };
    }
}
