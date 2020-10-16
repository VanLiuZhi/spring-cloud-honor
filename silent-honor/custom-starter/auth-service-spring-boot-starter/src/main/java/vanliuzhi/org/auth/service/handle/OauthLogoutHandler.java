package vanliuzhi.org.auth.service.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;
import vanliuzhi.org.common.starter.constant.AuthConstant;
import vanliuzhi.org.common.starter.utils.CommonStringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * @author Lys3415
 * @date 2020/10/14 16:47
 */
@Slf4j
public class OauthLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenStore tokenStore;

    /**
     * 退出登录，删除 刷新token 和 token
     */
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       Authentication authentication) {
        Assert.notNull(tokenStore, "tokenStore must be set");
        String token = extractToken(httpServletRequest);
        if (CommonStringUtil.isNotBlank(token)) {
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            OAuth2RefreshToken oAuth2RefreshToken;
            if (oAuth2AccessToken.getRefreshToken() != null) {
                log.info("remove refreshToken! {}", oAuth2AccessToken.getRefreshToken());
                oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
                // 调用接口删除 刷新token
                tokenStore.removeRefreshToken(oAuth2RefreshToken);
            }
            log.info("remove existing oAuth2AccessToken! {}", oAuth2AccessToken);
            // 调用接口删除 token
            tokenStore.removeAccessToken(oAuth2AccessToken);
        }
    }

    /**
     * 提取请求中的token
     */
    protected String extractToken(HttpServletRequest request) {
        String token = extractHeaderToken(request);
        if (token == null) {
            log.debug("Token not found in headers. Trying request parameters.");
            // token没有在请求头中，去请求参数中获取（该方法会从url参数或者表单中获取）
            // For HTTP servlets, parameters
            // are contained in the query string or posted form data.
            token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            if (token == null) {
                log.debug("Token not found in request parameters.  Not an OAuth2 request.");
            } else {
                // token 没有获取到，执行一次赋值操作，保证 OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE = Bearer
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);
            }
        }
        return token;
    }

    /**
     * 设置request的属性 OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE = Bearer
     * 提取token去掉类型头的部分，并返回
     */
    protected String extractHeaderToken(HttpServletRequest request) {
        // 从请求头的 AUTHORIZATION 字段取出token信息。Enumeration是枚举的封装
        Enumeration<String> header = request.getHeaders(AuthConstant.AUTHORIZATION);
        // 通常只有一个
        while (header.hasMoreElements()) {
            // 取得token
            String element = header.nextElement();
            // 全部转换成大写，如果是 BEARER 开头的，说明是我们定义的token
            if (element.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase())) {
                // 去掉Bearer部分，获取token
                String authHeaderValue = element.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                // 设置属性 OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE = Bearer
                request.setAttribute(
                        OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
                        element.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim());
                // 计算 ',' 所在索引
                int commaIndex = authHeaderValue.indexOf(",");
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
}
