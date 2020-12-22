package com.wangsheng.springcloud.common.model.github;
import lombok.Data;

@Data
public class GithubToken {
    private String access_token;
    private String token_type;
}
