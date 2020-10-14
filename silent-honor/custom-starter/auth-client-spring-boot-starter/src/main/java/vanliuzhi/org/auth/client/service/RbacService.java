package vanliuzhi.org.auth.client.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lys3415
 * @date 2020/10/14 14:32
 */
public interface RbacService {
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
