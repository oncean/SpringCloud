package com.wangsheng.springcloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.wangsheng.springcloud.common.model.auth.TokenResponse;
import com.wangsheng.springcloud.common.model.result.Result;
import com.wangsheng.springcloud.common.model.result.ResultCode;
import com.wangsheng.springcloud.exception.UnAuthorisedException;
import com.wangsheng.springcloud.service.LoginService;
import com.wangsheng.springcloud.service.RSAService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private RSAService rsaService;

    @Autowired
    private LoginService loginService;


    @ApiOperation(value = "获取加密公钥")
    @GetMapping("/getPublicKey")
    public Result<String> getPublicKey(){
        return Result.success(rsaService.getPublicKey());
    }


    @ApiOperation(value = "系统登录接口")
    @PostMapping("/system/login")
    public Result systemLogin(@RequestBody Map<String, String> body, HttpServletResponse response){
        String encrypt = body.get("encrypt");
        String decrypt = null;
        try {
            decrypt = rsaService.decrypt(encrypt);
        } catch (Exception e) {
            log.error("解密失败，登录错误",e);
            return Result.failed(ResultCode.USER_REQUEST_PARAM_ERROR);
        }
        Map<String,String> json = (Map<String, String>) JSONObject.parse(decrypt);
        TokenResponse result = loginService.sysLogin(json.get("username"),json.get("password"));
        String token = result.getAccess_token();
        if(StringUtils.isEmpty(token)){
            String error_description = result.getError_description();
            log.error("login failed, message is " + error_description);
            throw new UnAuthorisedException(ResultCode.USER_ACCESS_UNAUTHORIZED);
        }
        Cookie cookie = new Cookie("wangshengToken",result.getAccess_token());
        cookie.setDomain("wangsheng.com");
        cookie.setPath("/");
        response.addCookie(cookie);
        return Result.success(result);
    }
}
