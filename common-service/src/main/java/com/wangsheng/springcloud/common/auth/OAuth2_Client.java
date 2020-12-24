package com.wangsheng.springcloud.common.auth;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum OAuth2_Client implements Serializable {

    BROWSER(
            "browser",
            "browser",
            "all",
            "password"
    ),

    TEST1(
            "TEST1",
                    "TEST1",
                    "all",
                    "authorization_code"
    );

    private String client_id;
    private String client_secret;
    private String client_scope;

    //多个授权类型
    private String[] grant_type;


    OAuth2_Client(String client_id, String client_secret, String client_scope, String... grant_type) {
        this.client_id = client_id;
        this.client_scope= client_scope;
        this.client_secret = client_secret;
        this.grant_type = grant_type;
    }
}
