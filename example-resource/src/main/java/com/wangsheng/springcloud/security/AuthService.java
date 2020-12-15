package com.wangsheng.springcloud.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthService {
    public boolean canAccess(HttpServletRequest request, Authentication authentication) {
        return true;
    }
}
