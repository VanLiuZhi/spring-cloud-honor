package vanliuzhi.org.auth.server.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vanliuzhi.org.auth.server.entity.SysUser;
import vanliuzhi.org.auth.server.mapper.SysUserMapper;
import vanliuzhi.org.auth.server.oauth2.model.AuthUserDetail;
import vanliuzhi.org.auth.server.oauth2.service.MyUserDetailsService;

/**
 * 功能描述: 自定义UserDetailsService
 * 用户信息
 *
 * @author Trazen
 * @date 2020/7/14 15:43
 */
@Primary
@Service
@AllArgsConstructor
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Override
    public AuthUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        } else {
            return UserDetailConverter.convert(sysUser);
        }
    }

    public static class UserDetailConverter {
        static AuthUserDetail convert(SysUser user) {
            return new AuthUserDetail(user);
        }
    }
}
