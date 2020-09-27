package vanliuzhi.org.auth.server.oauth2.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import vanliuzhi.org.auth.server.entity.SysUser;

import java.util.Collections;

/**
 * 功能描述:
 *
 * @author Trazen
 * @date 2020/7/14 15:55
 */
public class AuthUserDetail extends User {

    @Setter
    @Getter
    private SysUser sysUser;

    public AuthUserDetail(SysUser user) {
        super(user.getUsername(), user.getPassword(), true, true,
                true, true, Collections.EMPTY_SET);
        this.sysUser = user;
    }
}
