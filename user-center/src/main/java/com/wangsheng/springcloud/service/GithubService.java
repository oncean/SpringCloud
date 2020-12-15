package com.wangsheng.springcloud.service;

import com.wangsheng.springcloud.model.GithubToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@RefreshScope
public class GithubService {

    private final String GET_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private final String client_id = "a00caa2d5e8032f8ad12";
    private final String client_secret = "bf0fb256bd312f38b52c5043c565f8a799fcf485";

//    @Value("${github.client_id}")
//    private String client_id;
//
//    @Value("${github.client_secret}")
//    private String client_secret;

    @Resource
    private RestTemplate restTemplate;

    public String getClientId(){
        return client_id;
    }

    public String loginGithub(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept","application/json");
                String url = GET_TOKEN_URL + "?client_id="+client_id+"&client_secret="+client_secret+"&code="+code;
        GithubToken result = restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(null,headers), GithubToken.class).getBody();
        return result.getAccess_token();
    }
}
