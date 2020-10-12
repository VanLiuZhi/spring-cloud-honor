package vanliuzhi.org.test.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Lys3415
 * @date 2020/9/30 11:39
 */
@EnableResourceServer
@Configuration
public class ResourceConfigurer extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    /**
     * 该⽅法⽤于定义资源服务器向远程认证服务器发起请求，进⾏token校验等事宜
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("cloud-test-server");
        // // 定义token服务对象（token校验就应该靠token服务对象）
        // RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        // // 校验端点/接⼝设置
        // remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9999/oauth/check_token");
        // // 携带客户端id和客户端安全码
        // remoteTokenServices.setClientId("vanliuzhi");
        // remoteTokenServices.setClientSecret("123456");
        // // 别忘了这⼀步
        // resources.tokenServices(remoteTokenServices);
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test").hasRole("admin")
                .anyRequest().authenticated();
    }
}
