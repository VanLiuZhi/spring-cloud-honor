package vanliuzhi.org.auth.service.token;

import org.apache.commons.collections.MapUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import vanliuzhi.org.auth.service.service.ValidateCodeService;
import vanliuzhi.org.common.starter.utils.CommonStringUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Lys3415
 * @date 2020/10/19 17:12
 */
public class PasswordEnhanceTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "password";

    private final AuthenticationManager authenticationManager;

    private final ValidateCodeService validateCodeService;

    public PasswordEnhanceTokenGranter(AuthenticationManager authenticationManager,
                                       AuthorizationServerTokenServices tokenServices,
                                       ClientDetailsService clientDetailsService,
                                       OAuth2RequestFactory requestFactory, ValidateCodeService validateCodeService) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory,
                GRANT_TYPE, validateCodeService);
    }

    protected PasswordEnhanceTokenGranter(AuthenticationManager authenticationManager,
                                          AuthorizationServerTokenServices tokenServices,
                                          ClientDetailsService clientDetailsService,
                                          OAuth2RequestFactory requestFactory, String grantType,
                                          ValidateCodeService validateCodeService) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
        this.validateCodeService = validateCodeService;
    }

    @Override
    @SuppressWarnings("all")
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        String password = parameters.get("password");

        // 终端
        String deviceId = MapUtils.getString(parameters, "deviceId");
        // 验证码
        String validCode = MapUtils.getString(parameters, "validCode");

        // 校验图形验证码
        if (CommonStringUtil.isNotBlank(deviceId) || CommonStringUtil.isNotEmpty(validCode)) {
            try {
                validateCodeService.validate(deviceId, validCode);
            } catch (Exception e) {
                throw new InvalidGrantException(e.getMessage());
            }
        }

        // Protect from downstream leaks of password
        // 移除账号，密码，验证码数据，防止数据泄露到下游
        parameters.remove("password");
        parameters.remove("deviceId");
        parameters.remove("validCode");

        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            throw new InvalidGrantException(ase.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + username);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
