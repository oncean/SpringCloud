package com.wangsheng.springcloud.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

public class CustomerReactiveJwtDecoder implements ReactiveJwtDecoder {

    @Override
    public Mono<Jwt> decode(String s) throws JwtException {
        return null;
    }
}
