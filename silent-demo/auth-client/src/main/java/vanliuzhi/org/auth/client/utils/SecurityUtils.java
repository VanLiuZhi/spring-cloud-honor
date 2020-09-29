package vanliuzhi.org.auth.client.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vanliuzhi.org.auth.client.model.CurrentUser;

/**
 * 功能描述:
 *
 * @author Trazen
 * @date 2020/7/15 9:28
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * get current login user info
     *
     * @return
     */
    public static CurrentUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? (CurrentUser) authentication.getPrincipal() : null;
    }
}
