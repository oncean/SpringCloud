package com.wangsheng.springcloud.security.config;

import cn.hutool.json.JSONUtil;
import com.wangsheng.springcloud.common.model.result.Result;
import com.wangsheng.springcloud.common.model.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

import static com.wangsheng.springcloud.common.auth.AuthConstants.COMMON_WHITE_URL_LIST;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Slf4j
public class WebSecurityConfig {
    //security的鉴权排除列表
    private static final String[] WHITE_URLS = COMMON_WHITE_URL_LIST;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // 未认证返回状态码
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(WHITE_URLS).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
                .and().authenticationEntryPoint(authenticationEntryPoint())
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and().csrf().disable(); //csrf存在session中,jwt无状态不需要
        return http.build();
    }


    /**
     * 未授权
     *
     * @return
     */
    @Bean
    ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> {
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                        response.getHeaders().set("Access-Control-Allow-Origin", "*");
                        response.getHeaders().set("Cache-Control", "no-cache");
                        String body = JSONUtil.toJsonStr(Result.failed(ResultCode.USER_ACCESS_UNAUTHORIZED));
                        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
                        return response.writeWith(Mono.just(buffer))
                                .doOnError(error -> DataBufferUtils.release(buffer));
                    });

            return mono;
        };
    }


    /**
     * token无效或者已过期自定义响应
     */
    @Bean
    ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, e) -> {
            Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> {
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                        response.getHeaders().set("Access-Control-Allow-Origin", "*");
                        response.getHeaders().set("Cache-Control", "no-cache");
                        String body = JSONUtil.toJsonStr(Result.failed(ResultCode.TOKEN_INVALID_OR_EXPIRED));
                        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
                        return response.writeWith(Mono.just(buffer))
                                .doOnError(error -> DataBufferUtils.release(buffer));
                    });
            return mono;
        };
    }

    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter(){
        return new ReactiveJwtAuthenticationConverterAdapter(new CustomerJwtAuthenticationConverter());
    }
}


