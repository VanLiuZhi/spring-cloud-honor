package vanliuzhi.org.auth.client.token;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;
import vanliuzhi.org.auth.client.service.SysClientService;

import javax.annotation.Resource;

/**
 * @author Lys3415
 * @date 2020/10/13 17:33
 */
public class AuthorizeConfigManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Resource
    private SysClientService sysClientService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * Determines if access is granted for a specific authentication and object.
     *
     * @param authentication the Authentication to check
     * @param object         the object to check
     * @return an decision or empty Mono if no decision could be made.
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        return null;
    }

    /**
     * Determines if access should be granted for a specific authentication and object
     *
     * @param authentication the Authentication to check
     * @param object         the object to check
     * @return an empty Mono if authorization is granted or a Mono error if access is
     * denied
     */
    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
        return null;
    }
}
