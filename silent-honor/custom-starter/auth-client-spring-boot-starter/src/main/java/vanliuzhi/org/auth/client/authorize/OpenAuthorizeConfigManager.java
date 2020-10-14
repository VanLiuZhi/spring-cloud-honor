package vanliuzhi.org.auth.client.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import vanliuzhi.org.auth.client.constant.AuthServiceConstant;
import vanliuzhi.org.auth.client.service.RbacService;

import java.util.List;

/**
 * @author Lys3415
 * @date 2020/10/14 14:22
 */
@Component
public class OpenAuthorizeConfigManager implements AuthorizeConfigManager {

    @Autowired
    private List<AuthorizeConfigProvider> authorizeConfigProviderList;

    @Autowired(required = false)
    private RbacService rbacService;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviderList) {
            authorizeConfigProvider.config(config);
        }
        int flag = 0;
        if (AuthServiceConstant.AUTH_SERVICE.equalsIgnoreCase(applicationName)) {
            // 认证中心
            flag = 2;
        }
        if (AuthServiceConstant.GATEWAY_SERVICE.equalsIgnoreCase(applicationName)) {
            // 网关
            flag = 1;
        }
        switch (flag) {
            case 1:
                // 方式1：网关根据API权限处理，根据应用分配服务权限，建议采用此方式
                // if(rbacService!=null){
                // 	config.anyRequest().access("@rbacService.hasPermission(request, authentication)") ;
                // }
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
