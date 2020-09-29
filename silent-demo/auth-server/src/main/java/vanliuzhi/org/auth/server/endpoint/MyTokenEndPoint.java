package vanliuzhi.org.auth.server.endpoint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author VanLiuZhi
 * @date 2020/9/27 14:42
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class MyTokenEndPoint {
    private final TokenStore tokenStore;

    @RequestMapping("/current/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
