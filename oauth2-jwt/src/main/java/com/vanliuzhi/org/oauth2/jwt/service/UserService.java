package com.vanliuzhi.org.oauth2.jwt.service;

import com.vanliuzhi.org.oauth2.jwt.module.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description Security 获取用户对象实现
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:25
 */
@Service
public class UserService implements UserDetailsService {

    private List<User> userList;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 初始化，模拟数据
     * <p>
     * PostConstruct 修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次，属于servlet 的规范
     * 注解的方法将会在依赖注入完成后被自动调用
     */
    @PostConstruct
    public void initData() {
        String password = passwordEncoder.encode("123456");
        userList = new ArrayList<>();
        userList.add(new User("macro", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
        userList.add(new User("andy", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
        userList.add(new User("mark", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<User> findUserList = userList.stream().filter(user -> user.getUsername().equals(username)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(findUserList)) {
            return findUserList.get(0);
        } else {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

    }
}
