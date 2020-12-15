package com.wangsheng.springcloud.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 *@program: cem
 *@description:
 *@author: yu meng hu
 *@create: 2019-11-13 22:34
 */
@Configuration
@EnableResourceServer
@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${cem.resource.server.config.isDev:true}")
    private Boolean isDev = false;


    private static final String[] ENDPOINTS = {"/actuator/health", "/actuator/env",
            "/actuator/metrics/**", "/actuator/trace", "/actuator/dump",
            "/actuator/jolokia", "/actuator/info", "/actuator/logfile", "/actuator/refresh",
            "/actuator/flyway", "/actuator/liquibase",
            "/actuator/heapdump", "/actuator/loggers", "/actuator/auditevents", "/actuator/env/PID",
            "/actuator/jolokia/**",
            "/v2/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**"};

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (isDev) {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers(ENDPOINTS)
                    .permitAll()
                    .antMatchers("/**").permitAll();
        } else {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers(ENDPOINTS)
                    .permitAll()
                    .antMatchers("/**").authenticated();
        }
    }
}
