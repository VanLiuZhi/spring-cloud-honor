package vanliuzhi.org.auth.center.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import vanliuzhi.org.auth.center.component.HxTokenEnhancer;

import java.util.ArrayList;
import java.util.List;

/**
 * 授权服务器配置
 *
 * @author VanLiuZhi
 * @date 2020/9/28 15:07
 */
@Configuration
public class OauthServerConfigurer extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理对象，自定义了用户信息，在 spring security 中配置成Bean交由IOC管理
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 获取用户详情，使用默认实现
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 将token转换成jwt
     */
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * token采用jwt存储
     */
    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    /**
     * token增强
     */
    @Autowired
    private HxTokenEnhancer tokenEnhancer;

    /**
     * 该对象用来将令牌信息存储到Redis中
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore inMemory() {
        return new InMemoryTokenStore();
    }

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
     * /oauth/check_token?token
     * 通过 get 请求带上token，可以去校验该token
     * <p>
     * 其它 Endpoint
     * /oauth/authorize(授权端，授权码模式使用)
     * /oauth/token(令牌端，获取 token)
     * /oauth/confirm_access(用户发送确认授权)
     * /oauth/error(认证失败)
     * /oauth/token_key(如果使用JWT，可以获的公钥用于 token 的验签)
     * 各个端点作用参考：cnblogs.com/Irving/p/9430460.html
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // 开启端⼝/oauth/token_key的访问权限（允许）
                .tokenKeyAccess("permitAll()")
                // 开启端⼝/oauth/check_token的访问权限（允许）
                .checkTokenAccess("permitAll()");
        // 表示支持 client_id 和 client_secret 做登录认证
        security.allowFormAuthenticationForClients();
    }

    /**
     * 用来配置客户端详情服务
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 这里显示配置，客户端是固定的，实际情况最好通过数据库获取客户端信息，方便扩展
        clients.inMemory()
                // 配置客户端id
                .withClient("vanliuzhi")
                // 授权模式为password和refresh_token两种
                .authorizedGrantTypes("password", "refresh_token")
                // 配置access_token的过期时间
                // .accessTokenValiditySeconds(1800)
                // 配置资源id(每个资源服务器都有唯一id，该方法接受的参数也是多个String，可以配置该客户端能访问的资源)
                .resourceIds("cloud-test-server")
                // 客户端的权限范围，此处配置为all全部即可
                .scopes("all")
                // 123456加密后的密码(客户端secret)
                .secret("$2a$10$tnj.nZjSzCBckTh2fRRK9.ZTYfU0y4pDiZZChKxxeOElBsxaQCn26");
    }

    /**
     * 来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore)
                // .tokenServices(authorizationServerTokenServices())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                // // 配置增强
                .tokenEnhancer(tokenEnhancerChain())
                .accessTokenConverter(jwtAccessTokenConverter);
    }

    /**
     * 该⽅法⽤户获取⼀个token服务对象（该对象描述了token有效期等信息）
     */
    private AuthorizationServerTokenServices authorizationServerTokenServices() {

        // 使⽤默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        // 是否开启令牌刷新
        defaultTokenServices.setSupportRefreshToken(true);
        // token以什么形式存储
        defaultTokenServices.setTokenStore(inMemory());
        // 设置令牌的有效时间 30 秒
        defaultTokenServices.setAccessTokenValiditySeconds(60 * 3);
        // 设置刷新令牌的有效时间 3天
        defaultTokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 *3);

        return defaultTokenServices;

    }

    private TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancerList = new ArrayList<>();
        enhancerList.add(jwtAccessTokenConverter);
        enhancerList.add(tokenEnhancer);
        tokenEnhancerChain.setTokenEnhancers(enhancerList);
        return tokenEnhancerChain;
    }
}
