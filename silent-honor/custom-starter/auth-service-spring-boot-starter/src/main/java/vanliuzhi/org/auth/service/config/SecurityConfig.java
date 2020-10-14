package vanliuzhi.org.auth.service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import vanliuzhi.org.common.starter.auth.props.PermitUrlProperties;

/**
 * Web Security 配置
 *
 * @author Lys3415
 * @date 2020/10/14 16:01
 */

// 开启方法权限校验
// @EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(PermitUrlProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
}
