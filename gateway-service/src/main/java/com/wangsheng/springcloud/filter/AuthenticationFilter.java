package com.wangsheng.springcloud.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

public class AuthenticationFilter extends AbstractGatewayFilterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);


    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(()->{
            List<String> gmAccountInfoJsons = exchange.getResponse().getHeaders().get("AccountInfo");
            exchange.getResponse().getHeaders().remove("AccountInfo");//移除包头中的用户信息不需要返回给客户端
            if(gmAccountInfoJsons != null && gmAccountInfoJsons.size() > 0) {
                String gmAccountInfoJson = gmAccountInfoJsons.get(0);//获取header中的用户信息
                //将信息放在session中，在后面认证的请求中使用
                exchange.getSession().block().getAttributes().put("AccountInfo", gmAccountInfoJson);
            }
            LOGGER.debug("登陆返回信息:{}",gmAccountInfoJsons);
        }));
    }
}
