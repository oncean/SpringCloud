# 解决无法获取权限的问题
## 问题

授权服务器是OAuth2的，对于系统本身用户分配了client，id为browser，scope为all，在资源服务器中获取到的authorities为scope而不是用户本身的系统内权限。



## 解决方案

### 授权服务器

授权服务器默认存储一个client为系统内用户集合，scope为`all`，实现`UserDetailsService`接口复写`loadUserByUsername`方法，将系统内权限传递给springsecurity，最后塞入jwt消息中

```java
/**
     * 客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     * @param clients
     * @throws Exception
     */
@Override
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(OAuth2_Client.BROWSER.getClient_id())
                .authorizedGrantTypes(OAuth2_Client.BROWSER.getGrant_type())
                .secret(OAuth2_Client.BROWSER.getClient_secret())
                .scopes(OAuth2_Client.BROWSER.getClient_scope())
                .authorities("admin","user")
                .accessTokenValiditySeconds(12*60*60);
}

@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("load user by username " + username);
    Result<User> userRes = userFeignService.getUserDetail(username);
    if (ResultCode.USER_NOT_EXIST.getCode().equals(userRes.getCode())) {
        throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
    }
    User user = userRes.getData();
    log.info("load user by username success");
    return new CustomerUserDetails(user);
}
```

生成的JWT消息示例

```json
{
 alg: "RS256",
 typ: "JWT"
}.
{
 sub: "admin",
 exp: 1608031046,
 authorities: [
  "ROLE_admin",
  "ROLE_user"
 ],
 jti: "5e408f2a-9924-4f03-8676-bcf7f31da42a",
 client_id: "browser",
 scope: [
  "all"
 ]
}.
[signature]
```

### 资源服务器

配置资源服务器的hasAnyAuthority中的参数只能判断scope的值

```java
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
         http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/test/user").hasAnyAuthority("all")
                )
                 .oauth2ResourceServer()
                 .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter(){
        return new CustomerJwtAuthenticationConverter();
    }
}
```

通过debug发现`JwtAuthenticationConverter`.`JwtGrantedAuthoritiesConverter`中`getAuthorities`方法默认获取的权限是`getAuthoritiesClaimName`方法中`Arrays.asList("scope", "scp")`

```java
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter
			= new JwtGrantedAuthoritiesConverter();

  /**
  * 从jwt中获取权限
  */
	@Override
	public final AbstractAuthenticationToken convert(Jwt jwt) {
		Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
		return new JwtAuthenticationToken(jwt, authorities);
	}
	@Deprecated
	protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		return this.jwtGrantedAuthoritiesConverter.convert(jwt);
	}

	public void setJwtGrantedAuthoritiesConverter(Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
		Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
		this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
	}
}


public final class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
	private static final String DEFAULT_AUTHORITY_PREFIX = "SCOPE_";

  /**
  * 默认的权限前缀
  */
	private static final Collection<String> WELL_KNOWN_AUTHORITIES_CLAIM_NAMES =
			Arrays.asList("scope", "scp");

	private String authorityPrefix = DEFAULT_AUTHORITY_PREFIX;

	private String authoritiesClaimName;

	...省略部分代码...

	
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String authority : getAuthorities(jwt)) {
			grantedAuthorities.add(new SimpleGrantedAuthority(this.authorityPrefix + authority));
		}
		return grantedAuthorities;
	}
  
	private Collection<String> getAuthorities(Jwt jwt) {
		String claimName = getAuthoritiesClaimName(jwt);

		if (claimName == null) {
			return Collections.emptyList();
		}

		Object authorities = jwt.getClaim(claimName);
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

```

尝试重写JwtAuthenticationConverter

```java
public class CustomerJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String AUTHORITIES_CLAIM_NAMES_SCOPE ="scope";
    private static final String AUTHORITIES_CLAIM_NAMES_SCOPE_NAME ="SCOPE_";

    private static final String AUTHORITIES_CLAIM_NAMES_AUTHS = "authorities";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
      	//将scope和内置权限全部塞入authorities里
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

```

并且在设置ServerHttpSecurity的时候添加上一句`.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())`

```java
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
```

