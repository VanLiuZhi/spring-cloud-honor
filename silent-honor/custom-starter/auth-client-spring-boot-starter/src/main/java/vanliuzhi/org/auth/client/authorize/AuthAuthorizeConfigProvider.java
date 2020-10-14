package vanliuzhi.org.auth.client.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import vanliuzhi.org.common.starter.auth.props.PermitUrlProperties;

/**
 * @author Lys3415
 * @date 2020/10/14 14:16
 */
@Component
@Order(Integer.MAX_VALUE - 1)
@EnableConfigurationProperties(PermitUrlProperties.class)
public class AuthAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Autowired(required = false)
    private PermitUrlProperties permitUrlProperties;

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config
                // 白名单免token认证
                .antMatchers(permitUrlProperties.getIgnored()).permitAll()
                // 监控端点免认证。EndpointRequest.toAnyEndpoint()可以获取全部的监控端点
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // HttpMethod.OPTIONS 请求放行，前后端分离需要配置
                .antMatchers(HttpMethod.OPTIONS).permitAll();
        return true;
    }
}
