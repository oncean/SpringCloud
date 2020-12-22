package com.wangsheng.springcloud;

import com.wangsheng.springcloud.exception.EnableExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
@EnableExceptionHandler
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }
}
