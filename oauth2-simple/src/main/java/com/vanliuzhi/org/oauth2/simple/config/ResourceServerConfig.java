package com.vanliuzhi.org.oauth2.simple.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;

/**
 * @Description 资源服务器配置
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:48
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfiguration {

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     super.configure(auth);
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .requestMatchers()
                .antMatchers("/user/**");
    }
}
