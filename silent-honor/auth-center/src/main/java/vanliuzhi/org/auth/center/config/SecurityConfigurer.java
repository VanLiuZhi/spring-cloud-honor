package vanliuzhi.org.auth.center.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Security 配置
 *
 * @author VanLiuZhi
 * @date 2020/9/28 15:15
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    /**
     * 注册⼀个认证管理器对象到容器，给授权服务器使用
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 注册⼀个用户详情对象到容器，给授权服务器使用
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    /**
     * 增加一个admin角色用户，一个user角色用户
     * 配置账号和密码，还有所属角色
     * admin ----123456 roles admin
     * liuzhi----123456 roles user
     *
     * @param auth AuthenticationManagerBuilder 用来创建 AuthenticationManager
     *             通过AuthenticationManagerBuilder来自定义用户信息，并把这个AuthenticationManager声明成Bean交给IOC管理
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // super.configure(auth); 不要调用父类方法
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("$2a$10$tnj.nZjSzCBckTh2fRRK9.ZTYfU0y4pDiZZChKxxeOElBsxaQCn26")
                .roles("admin")
                .and()
                .withUser("liuzhi")
                .password("$2a$10$tnj.nZjSzCBckTh2fRRK9.ZTYfU0y4pDiZZChKxxeOElBsxaQCn26")
                .roles("user");
    }

    /**
     * 端点拦截配置，除了 oauth/** 之外的所有端点都需要授权
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http); 不要调用父类方法
        http.antMatcher("/oauth/**").authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .and().csrf().disable();
    }
}
