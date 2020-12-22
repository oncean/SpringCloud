package com.wangsheng.springcloud.service;

import com.alibaba.fastjson.JSONObject;
import com.wangsheng.springcloud.common.auth.BasicAuth;
import com.wangsheng.springcloud.common.auth.OAuth2_Client;
import com.wangsheng.springcloud.common.model.auth.TokenResponse;
import com.wangsheng.springcloud.common.model.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sun.tools.jstat.Token;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LoginService {

    @Value("${server.port}")
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    public TokenResponse sysLogin(String userName,String password){
        Map<String,String> params = new LinkedHashMap<>();
        params.put("grant_type","password");
        params.put("username",userName);
        params.put("password",password);

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization","Basic " + BasicAuth.generate(
                OAuth2_Client.BROWSER.getClient_id(),
                OAuth2_Client.BROWSER.getClient_secret()
        ));

        HttpEntity httpEntity = new HttpEntity(params,headers);
        try{
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    "http://127.0.0.1:"+port+"/oauth/token?grant_type={grant_type}&username={username}&password={password}",
                    HttpMethod.POST,
                    httpEntity,
                    TokenResponse.class,
                    params
            );
            return response.getBody();
        }
        catch (HttpClientErrorException e){
            return JSONObject.parseObject(e.getResponseBodyAsString(),TokenResponse.class);
        }
    }
}
