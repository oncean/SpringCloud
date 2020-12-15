package com.wangsheng.springcloud.model;
import lombok.Data;

@Data
public class GithubToken {
    private String access_token;
    private String token_type;
}
