package com.vanliuzhi.org.custom.resource.server.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @Description 接口定义，授权配置Provider
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public interface AuthorizeConfigProvider {

    /**
     * @param config HttpSecurity安全配置器 ExpressionUrlAuthorizationConfigurer
     *               参考 https://blog.csdn.net/andy_zhang2007/article/details/93376098
     */
    boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
