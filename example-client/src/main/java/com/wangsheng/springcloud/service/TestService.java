package com.wangsheng.springcloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class TestService {

    @SentinelResource(value = "sayHello")
    public String sayHello(String name) {
        ReentrantLock reentrantLock = new ReentrantLock();
        return "Hello, " + name;
    }

}
