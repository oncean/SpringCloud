package com.wangsheng.springcloud.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Slf4j
public class CustomerJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String AUTHORITIES_CLAIM_NAMES_SCOPE ="scope";
    private static final String AUTHORITIES_CLAIM_NAMES_SCOPE_NAME ="SCOPE_";

    private static final String AUTHORITIES_CLAIM_NAMES_AUTHS = "authorities";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(buildAuthority(jwt,AUTHORITIES_CLAIM_NAMES_AUTHS));
        authorities.addAll(buildAuthority(jwt,AUTHORITIES_CLAIM_NAMES_SCOPE));

        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * 从jwt的scope和authorities组装权限
     * @param jwt
     * @param key
     * @return
     */
    private Collection<GrantedAuthority> buildAuthority(Jwt jwt,String key){
        String authorityPrefix = AUTHORITIES_CLAIM_NAMES_SCOPE.equals(key)?AUTHORITIES_CLAIM_NAMES_SCOPE_NAME:"";
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : getAuthorities(jwt,key)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authorityPrefix + authority));
        }
        return grantedAuthorities;
    }

    /**
     * 从jwt中获取指定的字段内容
     * @param jwt
     * @param key
     * @return
     */
    private Collection<String> getAuthorities(Jwt jwt,String key) {

        Object authorities = jwt.getClaim(key);
        if (authorities instanceof String) {
            if (StringUtils.hasText((String) authorities)) {
                return Arrays.asList(((String) authorities).split(" "));
            } else {
                return Collections.emptyList();
            }
        } else if (authorities instanceof Collection) {
            return (Collection<String>) authorities;
        }

        return Collections.emptyList();
    }
}
