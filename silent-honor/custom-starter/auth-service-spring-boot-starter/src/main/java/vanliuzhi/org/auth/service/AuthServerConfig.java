package vanliuzhi.org.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import vanliuzhi.org.auth.service.service.RedisClientDetailsService;
import vanliuzhi.org.auth.service.service.ValidateCodeService;
import vanliuzhi.org.auth.service.token.PasswordEnhanceTokenGranter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lys3415
 */
@Configuration
public class AuthServerConfig {

    @Bean
    public RedisClientDetailsService redisClientDetailsService(DataSource dataSource, RedisTemplate<String, Object> redisTemplate) {
        RedisClientDetailsService redisClientDetailsService = new RedisClientDetailsService(dataSource);
        redisClientDetailsService.setRedisTemplate(redisTemplate);
        return redisClientDetailsService;
    }

    // @Bean
    // public RandomValueAuthorizationCodeServices authorizationCodeServices(RedisTemplate<String, Object> redisTemplate) {
    //     new RedisAuthorizationCodeServices
    // }

    @Configuration
    @EnableAuthorizationServer
    @AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
    public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
        /**
         * 注入authenticationManager 来支持 password grant type
         */
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private ValidateCodeService validateCodeService;

        @Autowired(required = false)
        private TokenStore tokenStore;

        @Autowired(required = false)
        private JwtAccessTokenConverter jwtAccessTokenConverter;

        @Autowired
        private WebResponseExceptionTranslator webResponseExceptionTranslator;

        @Autowired
        private RedisClientDetailsService redisClientDetailsService;

        /**
         * 配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            //通用处理
            endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager)
                    // 支持
                    .userDetailsService(userDetailsService);

            // if (tokenStore instanceof JwtTokenStore) {
            //     endpoints.accessTokenConverter(jwtAccessTokenConverter);
            // }

            //处理授权码
            // endpoints.authorizationCodeServices(authorizationCodeServices);
            // 处理 ExceptionTranslationFilter 抛出的异常
            endpoints.exceptionTranslator(webResponseExceptionTranslator);

            //处理oauth 模式
            ClientDetailsService clientDetails = endpoints.getClientDetailsService();
            AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
            AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
            OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();

            //tokenGranters添加oauth模式 ，可以让/oauth/token支持自定义模式，继承AbstractTokenGranter 扩展
            List<TokenGranter> tokenGranters = new ArrayList<>();
            //客户端模式   GRANT_TYPE = "client_credentials";
            tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
            //密码模式	  GRANT_TYPE = "password";
            tokenGranters.add(new PasswordEnhanceTokenGranter(authenticationManager, tokenServices, clientDetails,
                    requestFactory, validateCodeService));
            //授权码模式   GRANT_TYPE = "authorization_code";
            tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails, requestFactory));
            //刷新模式	  GRANT_TYPE = "refresh_token";
            tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
            //简易模式	  GRANT_TYPE = "implicit";
            tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory));
            //短信模式	  GRANT_TYPE = "sms"; 参考ResourceOwnerPasswordTokenGranter重写
            // tokenGranters.add(new SMSCodeTokenGranter( userDetailsService,  validateCodeService  ,  tokenServices, clientDetails, requestFactory));
            // CompositeTokenGranter 能够作为容器存储各种模式，通过grantType来决定使用哪个模式
            endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));
        }

        /**
         * 配置应用名称 应用id
         * 配置OAuth2的客户端相关信息
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            // redisClientDetailsService 是基于 JDBC 的增强版，数据会被缓存在redis中，减少对数据库的访问
            clients.withClientDetails(redisClientDetailsService);
            // 执行一次加载缓存操作，把客户端数据从数据库读入缓存
            redisClientDetailsService.loadAllClientToCache();
        }

        /**
         * 对应于配置AuthorizationServer安全认证的相关信息，创建ClientCredentialsTokenEndpointFilter核心过滤器
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            // url:/oauth/token_key,exposes
            security.tokenKeyAccess("permitAll()")
                    /// public key for token
                    /// verification if using
                    /// JWT tokens
                    // url:/oauth/check_token
                    .checkTokenAccess("isAuthenticated()")
                    // allow check token
                    .allowFormAuthenticationForClients();
        }

    }

}
