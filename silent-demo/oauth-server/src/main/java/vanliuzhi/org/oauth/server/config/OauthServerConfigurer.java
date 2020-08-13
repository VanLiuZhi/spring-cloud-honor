package vanliuzhi.org.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author: 190coder <190coder.cn>
 * @description: 当前类为Oauth2 server的配置类（需要继承特定的⽗类)
 * @create: 2020-07-30 20:03
 */
@Configuration
@EnableAuthorizationServer
public class OauthServerConfigurer extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 认证服务器最终是以api接⼝的⽅式对外提供服务（校验合法性并⽣成令牌、校验令牌等）
     * 那么，以api接⼝⽅式对外的话，就涉及到接⼝的访问权限，我们需要在这⾥进⾏必要的配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);

        // 相当于打开endpoints 访问接⼝的开关，这样的话后期我们能够访问该接⼝
        security.
                // 允许客户端表单认证
                        allowFormAuthenticationForClients()
                // 开启端⼝/oauth/token_key的访问权限（允许）
                .tokenKeyAccess("permitAll()")
                // 开启端⼝/oauth/check_token的访问权限（允许）
                .checkTokenAccess("permitAll()");

    }

    /**
     * 客户端详情配置，
     * ⽐如client_id，secret
     * 当前这个服务就如同QQ平台，拉勾⽹作为客户端需要qq平台进⾏登录授权认证等，提前需
     * 要到QQ平台注册，QQ平台会给拉勾⽹
     * 颁发client_id等必要参数，表明客户端是谁
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);

        clients.
                // 客户端信息存储在什么地⽅，可以在内存中，可以在数据库⾥
                        inMemory()
                // 添加⼀个client配置,指定其client_id
                .withClient("client_lagou")
                // 指定客户端的密码/安全码
                .secret("abcxyz")
                // 指定客户端所能访问资源
                .resourceIds("autodeliver")
                // 认证类型/令牌颁发模式，可以配置多个在这⾥，但是不⼀定都⽤，具体使⽤哪种⽅式颁发token，需要客户端调⽤的时候传递参数指定
                .authorizedGrantTypes("password", "refresh_token")
                // 客户端的权限范围，此处配置为all全部即可
                .scopes("all");
    }

    /**
     * 认证服务器是玩转token的，那么这⾥配置token令牌管理相关（token此时就是⼀个字符
     * 串，当下的token需要在服务器端存储，那么存储在哪⾥呢？都是在这⾥配置）
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);

        endpoints
                // 指定token的存储⽅法
                .tokenStore(tokenStore())
                //token服务的⼀个描述，可以认为是token⽣成细节的描述，⽐如有效时间多少等
                .tokenServices(authorizationServerTokenServices())
                // 指定认证管 理器，随后注⼊⼀个到当前类使⽤即可
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
     * 该⽅法⽤户获取⼀个token服务对象（该对象描述了token有效期等信息）
     *
     * @return
     */
    private AuthorizationServerTokenServices authorizationServerTokenServices() {

        // 使⽤默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

        // 是否开启令牌刷新
        defaultTokenServices.setSupportRefreshToken(true);
        // token以什么形式存储
        defaultTokenServices.setTokenStore(tokenStore());
        // access_token就是我们请求资源需要携带的令牌
        defaultTokenServices.setAccessTokenValiditySeconds(30);
        // 设置刷新令牌的有效时间 3天
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);

        return defaultTokenServices;

    }

    /**
     * 该⽅法⽤于创建tokenStore对象（令牌存储对象）token以什么形式存储
     */
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}

