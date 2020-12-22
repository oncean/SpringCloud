package com.wangsheng.springcloud.common.auth;

import java.util.Base64;

public class BasicAuth {
    public static String generate(String username,String password){
        return Base64.getEncoder().encodeToString((username+":"+password).getBytes());
    }
}
