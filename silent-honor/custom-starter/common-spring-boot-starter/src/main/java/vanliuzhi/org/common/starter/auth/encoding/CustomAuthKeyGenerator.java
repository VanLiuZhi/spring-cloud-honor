package vanliuzhi.org.common.starter.auth.encoding;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

/**
 * @author VanLiuZhi
 * @date 2020/10/12 21:55
 */
public class CustomAuthKeyGenerator implements AuthenticationKeyGenerator
{
    @Override
    public String extractKey(OAuth2Authentication authentication) {
        return null;
    }
}
