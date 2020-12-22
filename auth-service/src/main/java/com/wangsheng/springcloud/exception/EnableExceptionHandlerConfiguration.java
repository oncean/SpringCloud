package com.wangsheng.springcloud.exception;

import org.springframework.context.annotation.Bean;


public class EnableExceptionHandlerConfiguration {
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(){
        return new GlobalExceptionHandler();
    }
}
