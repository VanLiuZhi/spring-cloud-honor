package vanliuzhi.org.auth.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import vanliuzhi.org.auth.service.constant.SecurityConstant;
import vanliuzhi.org.auth.service.handle.OauthLogoutHandler;
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

    /**
     * 登录成功处理
     */
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * 登录失败处理
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 自定义异常处理端口 默认空
     */
    @Autowired(required = false)
    private AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 用户详情服务，由引用此模块的服务装配
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 退出登录处理
     */
    @Autowired
    private OauthLogoutHandler oauthLogoutHandler;

    @Autowired
    private PermitUrlProperties permitUrlProperties;

    // @Autowired
    // private SmsCodeAuthenticationProvider smsCodeAuthenticationProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**", "/doc.html", "/login.html");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/health");
        // 忽略登录界面
        web.ignoring().antMatchers("/login.html");
        web.ignoring().antMatchers("/index.html");
        web.ignoring().antMatchers("/oauth/user/token");
        web.ignoring().antMatchers("/oauth/client/token");
        web.ignoring().antMatchers("/validata/code/**");
        web.ignoring().antMatchers("/sms/**");
        web.ignoring().antMatchers("/authentication/**");
        web.ignoring().antMatchers(permitUrlProperties.getIgnored());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().loginPage(SecurityConstant.LOGIN_PAGE).loginProcessingUrl(SecurityConstant.LOGIN_PROCESSING_URL)
                .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);

        if (authenticationEntryPoint != null) {
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
            // 关闭session
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        } else {
            // 使用 IF_REQUIRED 模式支持session，在需要的时候会创建
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        }

        http.logout().logoutSuccessUrl(SecurityConstant.LOGIN_PAGE)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .addLogoutHandler(oauthLogoutHandler).clearAuthentication(true);

        //注册到AuthenticationManager中去 增加支持SmsCodeAuthenticationToken
        // http.authenticationProvider(smsCodeAuthenticationProvider);

        // http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();
        http.headers().cacheControl();
    }

    /**
     * 全局用户信息
     *
     * @param auth 认证管理
     * @throws Exception 用户认证异常信息
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
