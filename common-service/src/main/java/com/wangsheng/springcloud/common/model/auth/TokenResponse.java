package com.wangsheng.springcloud.common.model.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenResponse implements Serializable {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String scope;
    private String jti;
    private String error;
    private String error_description;
}
