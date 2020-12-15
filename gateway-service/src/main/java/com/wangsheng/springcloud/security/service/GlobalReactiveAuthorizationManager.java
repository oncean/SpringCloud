package com.wangsheng.springcloud.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class GlobalReactiveAuthorizationManager  implements ReactiveAuthorizationManager<AuthorizationContext> {

    private List<String> authorities = new ArrayList<>();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        return mono
                .filter(a->a.isAuthenticated())
                .flatMapIterable(a->a.getAuthorities())
                .map(g->g.getAuthority())
                .any(c->{
                    String[] roles = c.split(",");
                    for (String role :
                            roles) {
                        if(authorities.contains(role)){
                            return true;
                        }
                    }
                    return false;
                })
                .map(hasAuthority -> new AuthorizationDecision(hasAuthority))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
