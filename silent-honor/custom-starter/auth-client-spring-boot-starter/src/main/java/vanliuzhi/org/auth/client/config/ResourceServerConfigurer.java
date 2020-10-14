// package vanliuzhi.org.auth.client.config;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
// import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
// import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
// import vanliuzhi.org.auth.client.properties.AuthClientProperties;
//
// /**
//  * 资源服务器配置
//  *
//  * @author VanLiuZhi
//  * @date 2020/9/28 13:57
//  */
// @Slf4j
// @Configuration
// public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
//
//     @Autowired
//     private AuthClientProperties authClientProperties;
//
//     @Autowired
//     private RedisConnectionFactory redisConnectionFactory;
//
//     @Override
//     public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//         // 从配置文件读取设置资源服务器id
//         System.out.println(authClientProperties.getResourceId());
//         if (authClientProperties.getResourceId() != null) {
//             resources.resourceId(authClientProperties.getResourceId())
//                     // 指明该资源只能基于令牌访问，默认true
//                     .stateless(true);
//         }
//         // 这里的签名key 保持和认证中心一致
//         if (authClientProperties.getSigningKey() == null) {
//             log.info("SigningKey is null cant not decode token.......");
//         }
//
//         // resources.tokenStore(new RedisTokenStore(redisConnectionFactory));
//
//         // DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
//         // accessTokenConverter.setUserTokenConverter(new MyUserAuthenticationConverter());
//         //
//         // JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//         // //设置解析jwt的密钥
//         // converter.setSigningKey(authClientProperties.getSigningKey());
//         // converter.setVerifier(new MacSigner(authClientProperties.getSigningKey()));
//         //
//         // MyTokenServices tokenServices = new MyTokenServices();
//         //
//         // // 在CustomTokenServices注入三个依赖对象
//         // //设置token存储策略
//         // tokenServices.setTokenStore(new JwtTokenStore(converter));
//         // tokenServices.setJwtAccessTokenConverter(converter);
//         // tokenServices.setDefaultAccessTokenConverter(accessTokenConverter);
//         // tokenServices.setRestTemplate(lbRestTemplate);
//         // resources.tokenServices(tokenServices)
//         //         .authenticationEntryPoint(baseAuthenticationEntryPoint);
//
//     }
//
//     /**
//      * Spring Security 中的配置优先级高于资源服务器中的配置，即请求地址先经过
//      * Spring Security 的 HttpSecurity，再经过资源服务器的 HttpSecurity
//      */
//     @Override
//     public void configure(HttpSecurity http) throws Exception {
//         super.configure(http);
//         // 关闭csrf
//         http.csrf().disable();
//         // 放行 swagger ui 相关端点
//         http.authorizeRequests().antMatchers(
//                 "/v2/api-docs",
//                 "/swagger-resources/configuration/ui",
//                 "/swagger-resources",
//                 "/swagger-resources/configuration/security",
//                 "/swagger-ui.html",
//                 "/webjars/**",
//                 "/api/**/v2/api-docs")
//                 .permitAll();
//         // 根据自定义配置url放行
//         if (authClientProperties.getIgnoreUrls() != null) {
//             for (String url : authClientProperties.getIgnoreUrls()) {
//                 http.authorizeRequests().antMatchers(url).permitAll();
//             }
//         }
//         // 其他请求均需要token才能访问
//         http.authorizeRequests()
//                 .antMatchers("/test").hasRole("user")
//                 .antMatchers("/user/**").hasRole("admin")
//                 .anyRequest().authenticated();
//         // http.authorizeRequests().anyRequest().authenticated();
//     }
//
// }
