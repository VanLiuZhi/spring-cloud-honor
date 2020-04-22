package com.vanliuzhi.org.custom.resource.server.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @Description 接口定义，授权配置管理器
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public interface AuthorizeConfigManager {
    /**
     * @param config HttpSecurity安全配置器 ExpressionUrlAuthorizationConfigurer
     */
    void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
