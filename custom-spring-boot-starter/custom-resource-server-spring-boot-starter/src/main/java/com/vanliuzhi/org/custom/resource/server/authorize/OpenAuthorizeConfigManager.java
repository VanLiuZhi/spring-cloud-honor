package com.vanliuzhi.org.custom.resource.server.authorize;

import com.vanliuzhi.org.custom.resource.server.constant.AuthServiceConstant;
import com.vanliuzhi.org.custom.resource.server.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Component
@SuppressWarnings("all")
public class OpenAuthorizeConfigManager implements AuthorizeConfigManager {

    @Autowired
    private List<AuthorizeConfigProvider> authorizeConfigProviders;

    @Autowired(required = false)
    private RbacService rbacService;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

        // 设置访问
        for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
            authorizeConfigProvider.config(config);
        }
        // 根据当前应用做鉴权配置。此时config已经配置过白名单了，接下来决定其它URL使用什么规则
        int flag = 0;

        if (AuthServiceConstant.GATEWAY_SERVICE.equalsIgnoreCase(applicationName)) {
            //网关API权限
            flag = 1;
        } else if (AuthServiceConstant.AUTH_SERVICE.equalsIgnoreCase(applicationName)) {
            //认证中心
            flag = 2;
        }
        switch (flag) {
            case 1:
                // 方式1：网关根据API权限处理，根据应用分配服务权限，建议采用此方式
                // config
                // .anyRequest()
                // .access("@rbacService.hasPermission(request, authentication)") ;
                // 方式2：强制校验token 非API权限处理，此方式不需要页面根据应用分配服务权限
                config.anyRequest().authenticated();
                break;
            case 2:
                // 认证中心 强制校验token
                config.anyRequest().authenticated();
                break;
            default:
                // 方式1：非网关可以采用不强制鉴权模式
                // config.anyRequest().permitAll();
                // 方式2：强制校验token
				// config.anyRequest().authenticated();
                config.anyRequest().permitAll();
        }
    }

}
