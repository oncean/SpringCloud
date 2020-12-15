package com.wangsheng.springcloud.security.config;

import com.wangsheng.springcloud.security.service.GlobalReactiveAuthorizationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
@Slf4j
public class WebSecurityConfig {
    //security的鉴权排除列表
    private static final String[] excludedAuthPages = {
            "/test/**",
            "/login",
            "/auth/login",
            "/auth/test",
            "/auth/logout",
            "/health",
            "/api/socket/**",
            "/gitHub/getClientId",
            "gitHub/callback"
    };


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // 未认证返回状态码
        //HttpStatusServerEntryPoint loginPoint = new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED);
        http
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .csrf().disable(); //csrf存在session中
        return http.build();
    }
}
