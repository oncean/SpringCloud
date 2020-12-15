package com.wangsheng.springcloud.service;

import com.wangsheng.springcloud.domian.CustomerUserDetails;
import com.wangsheng.springcloud.model.result.Result;
import com.wangsheng.springcloud.model.result.ResultCode;
import com.wangsheng.springcloud.model.usercenter.User;
import com.wangsheng.springcloud.openFeign.UserFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 自定义用户认证和授权
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserFeignService userFeignService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("load user by username " + username);
        Result<User> userRes = userFeignService.getUserDetail(username);
        if (ResultCode.USER_NOT_EXIST.getCode().equals(userRes.getCode())) {
            throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
        }
        User user = userRes.getData();
        log.info("load user by username success");
        return new CustomerUserDetails(user);
    }

    @Bean
    public PasswordEncoder customerPasswordEncode(){
        return new PasswordEncoder(){

            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

}
