package vanliuzhi.org.auth.center.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import vanliuzhi.org.auth.center.component.LagouAccessTokenConvertor;

/**
 * JWT存储配置
 *
 * @author VanLiuZhi
 * @date 2020/9/28 15:30
 */
@Configuration
public class JwtTokenStoreConfig {

    private final static String SIGN_KEY = "test_key";

    @Autowired
    private LagouAccessTokenConvertor lagouAccessTokenConvertor;

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();

        // 签名密钥
        accessTokenConverter.setSigningKey(SIGN_KEY);
        // 验证时使⽤的密钥，和签名密钥保持⼀致
        accessTokenConverter.setVerifier(new MacSigner(SIGN_KEY));

        // accessTokenConverter.setAccessTokenConverter(lagouAccessTokenConvertor);

        return accessTokenConverter;
    }
}
