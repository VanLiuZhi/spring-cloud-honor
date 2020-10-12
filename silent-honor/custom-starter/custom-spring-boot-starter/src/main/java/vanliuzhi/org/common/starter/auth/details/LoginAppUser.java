package vanliuzhi.org.common.starter.auth.details;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import vanliuzhi.org.common.starter.model.SysRole;
import vanliuzhi.org.common.starter.model.SysUser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义LoginAppUser，实现UserDetails接口，需要实现以下方法
 * <p>
 * getAuthorities()       用户的权限集， 默认需要添加ROLE_ 前缀
 * getPassword()          用户的加密后的密码， 不加密会使用{noop}前缀，SysUser中实现
 * getUsername()          应用内唯一的用户名，SysUser中实现
 * isAccountNonExpired()  账户是否过期
 * isAccountNonLocked()   账户是否锁定
 * isCredentialsNonExpired() 凭证是否过期
 * isEnabled() 用户是否可用，以上3个状态都默认为true，isEnabled通过读取SysUser属性实现
 *
 * @author Lys3415
 * @date 2020/10/12 16:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginAppUser extends SysUser implements UserDetails {
    private final static String PERMISSIONS_ROLE = "ROLE_";
    private Set<SysRole> sysRoles;
    private Set<String> permissions;

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     * 用户的权限集
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> collection = new HashSet<>();
        Collection<GrantedAuthority> synchronizedSet = Collections.synchronizedSet(collection);
        if (!CollectionUtils.isEmpty(sysRoles)) {
            sysRoles.parallelStream().forEach(sysRole -> {
                String role = sysRole.getCode().startsWith("ROLE_") ? sysRole.getCode() : PERMISSIONS_ROLE + sysRole.getCode();
                synchronizedSet.add(new SimpleGrantedAuthority(role));
            });
        }
        if (!CollectionUtils.isEmpty(permissions)) {
            permissions.parallelStream().forEach(per -> {
                synchronizedSet.add(new SimpleGrantedAuthority(per));
            });
        }
        return collection;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     * <p>
     * 指明身份凭证是否过期(比如使用密码作为凭证)，过期了则无法认证
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     * <p>
     * 指明用户是否可用，不可用无法认证
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return getEnabled();
    }
}
