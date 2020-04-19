package com.vanliuzhi.org.oauth2.simple.config;

import com.vanliuzhi.org.oauth2.simple.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

import javax.annotation.Resource;

/**
 * @Description 授权服务器配置
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:41
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    // @Override
    // public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    //     super.configure(security);
    // }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("admin")
                .secret(passwordEncoder.encode("123456"))
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600 * 60)
                .redirectUris("http://www.baidu.com")
                .scopes("all")
                .authorizedGrantTypes("authorization_code", "password");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).userDetailsService(userService);
    }
}
