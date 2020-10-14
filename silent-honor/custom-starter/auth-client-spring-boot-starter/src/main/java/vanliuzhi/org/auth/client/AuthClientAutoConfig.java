package vanliuzhi.org.auth.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import vanliuzhi.org.auth.client.authorize.AuthorizeConfigManager;
import vanliuzhi.org.auth.client.config.ResourceServerConfigurer;
import vanliuzhi.org.common.starter.auth.props.PermitUrlProperties;

import javax.annotation.Resource;

/**
 * @author VanLiuZhi
 * @date 2020/9/28 14:00
 */
@EnableResourceServer
@Configuration
@AutoConfigureAfter(TokenStore.class)
@Import({ResourceServerConfigurer.class})
@EnableConfigurationProperties(PermitUrlProperties.class)
public class AuthClientAutoConfig extends ResourceServerConfigurerAdapter {

    /**
     * 对应oauth_client_details的 resource_ids字段 如果表中有数据
     * client_id只能访问响应resource_ids的资源服务器
     */
    private static final String DEMO_RESOURCE_ID = "";

    @Resource
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Autowired
    private OAuth2WebSecurityExpressionHandler expressionHandler;

    @Autowired
    private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;

    @Autowired
    private PermitUrlProperties permitUrlProperties;

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(permitUrlProperties.getIgnored());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        if (tokenStore != null) {
            resources.tokenStore(tokenStore);
        }
        resources.stateless(true);
        resources.authenticationEntryPoint(authenticationEntryPoint);
        resources.expressionHandler(expressionHandler);
        resources.accessDeniedHandler(oAuth2AccessDeniedHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        authorizeConfigManager.config(http.authorizeRequests());
    }
}
