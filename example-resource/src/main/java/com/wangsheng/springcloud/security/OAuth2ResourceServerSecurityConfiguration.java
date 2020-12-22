package com.wangsheng.springcloud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
         http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/test/user").hasAnyAuthority("ROLE_admin","ROLE_user")
                )
                 .oauth2ResourceServer()
                 .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter(){
        return new CustomerJwtAuthenticationConverter();
    }
}
