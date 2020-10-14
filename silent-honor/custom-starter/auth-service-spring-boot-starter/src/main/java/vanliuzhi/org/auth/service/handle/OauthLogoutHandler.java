package vanliuzhi.org.auth.service.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;
import vanliuzhi.org.common.starter.constant.AuthConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * @author Lys3415
 * @date 2020/10/14 16:47
 */
public class OauthLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       Authentication authentication) {
        Assert.notNull(tokenStore, "tokenStore must be set");
        // String token = e

    }

    // protected String extractToken(HttpServletRequest request) {
    //
    // }

    protected String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> header = request.getHeaders(AuthConstant.AUTHORIZATION);
        // 通常只有一个
        while (header.hasMoreElements()) {
            String element = header.nextElement();
            if (element.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase())) {
                String authHeaderValue  = element.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim();
                // request.setAttribute(
                //         OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, );
            }
        }
        return "";


    }
}
