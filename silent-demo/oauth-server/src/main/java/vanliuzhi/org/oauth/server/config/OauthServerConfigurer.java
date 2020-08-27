package vanliuzhi.org.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
 * 授权服务器配置
 *
 * @author lys3415
 */
@Configuration
@EnableAuthorizationServer
public class OauthServerConfigurer extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理对象，使用默认实现
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 该对象用来将令牌信息存储到内存中
     * OAuth2令牌持久化主要有以下几种
     * <p>
     * 1. InMemoryTokenStore  内存存储 OAuth2默认储存方式
     * 2. JdbcTokenStore  数据库存储
     * 3. RedisTokenStore Redis存储
     * 4. JwkTokenStore & JwtTokenStore
     */
    @Autowired(required = false)
    private TokenStore inMemoryTokenStore;

    /**
     * 获取用户详情
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 指定密码的加密方式
     * 推荐使用 BCryptPasswordEncoder, Pbkdf2PasswordEncoder, SCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用BCrypt强哈希函数加密方案（密钥迭代次数默认为10）
        return new BCryptPasswordEncoder();
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // super.configure(security);
        //
        // // 相当于打开endpoints 访问接⼝的开关，这样的话后期我们能够访问该接⼝
        // security.
        //         // 允许客户端表单认证
        //                 allowFormAuthenticationForClients()
        //         // 开启端⼝/oauth/token_key的访问权限（允许）
        //         .tokenKeyAccess("permitAll()")
        //         // 开启端⼝/oauth/check_token的访问权限（允许）
        //         .checkTokenAccess("permitAll()");
        // 表示支持 client_id 和 client_secret 做登录认证
        security.allowFormAuthenticationForClients();
    }

    /**
     * 用来配置客户端详情服务
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // super.configure(clients);

        // clients.
        //         // 客户端信息存储在什么地⽅，可以在内存中，可以在数据库⾥
        //                 inMemory()
        //         // 添加⼀个client配置,指定其client_id
        //         .withClient("client")
        //         // 指定客户端的密码/安全码
        //         .secret("abcxyz")
        //         // 指定客户端所能访问资源
        //         .resourceIds("autodeliver")
        //         // 认证类型/令牌颁发模式，可以配置多个在这⾥，但是不⼀定都⽤，具体使⽤哪种⽅式颁发token，需要客户端调⽤的时候传递参数指定
        //         .authorizedGrantTypes("password", "refresh_token")
        //         // 客户端的权限范围，此处配置为all全部即可
        //         .scopes("all");

        // 这里显示配置，客户端是固定的，实际情况最好通过数据库获取客户端信息，方便扩展
        clients.inMemory()
                .withClient("vanliuzhi")
                //授权模式为password和refresh_token两种
                .authorizedGrantTypes("password", "refresh_token")
                // 配置access_token的过期时间
                .accessTokenValiditySeconds(1800)
                //配置资源id
                .resourceIds("myResource")
                // 客户端的权限范围，此处配置为all全部即可
                .scopes("all")
                //123加密后的密码
                .secret("$2a$10$tnj.nZjSzCBckTh2fRRK9.ZTYfU0y4pDiZZChKxxeOElBsxaQCn26");
    }

    /**
     * 来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // super.configure(endpoints);
        //
        // endpoints
        //         // 指定token的存储⽅法
        //         .tokenStore(tokenStore())
        //         //token服务的⼀个描述，可以认为是token⽣成细节的描述，⽐如有效时间多少等
        //         .tokenServices(authorizationServerTokenServices())
        //         // 指定认证管 理器，随后注⼊⼀个到当前类使⽤即可
        //         .authenticationManager(authenticationManager)
        //         .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        //配置令牌的存储（这里存放在内存中）
        endpoints.tokenStore(inMemoryTokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
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

