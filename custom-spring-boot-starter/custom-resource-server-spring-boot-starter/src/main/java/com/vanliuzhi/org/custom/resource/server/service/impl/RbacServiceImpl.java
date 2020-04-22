package com.vanliuzhi.org.custom.resource.server.service.impl;

import com.vanliuzhi.org.custom.resource.server.dao.SysClientDao;
import com.vanliuzhi.org.custom.resource.server.dao.SysServiceDao;
import com.vanliuzhi.org.custom.resource.server.service.RbacService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description API 级别权限认证
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Service("rbacService")
@SuppressWarnings("all")
public class RbacServiceImpl implements RbacService {

    // sys_service 表操作 DAO
    @Resource
    private SysServiceDao sysServiceDao;

    // oauth_client_details 表操作 DAO
    @Resource
    private SysClientDao sysClientDao;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * @param request        HttpServletRequest
     * @param authentication 认证信息
     * @return 是否有权限
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Authentication user = SecurityContextHolder.getContext()
                .getAuthentication();
        // TODO 目前都是true
        // 根据当前请求头中的数据，得到对应的 auth_client，通过id去查sys_service，匹配sys_service的path和请求url
        boolean hasPermission = false;
        if (user != null) {
            if (user instanceof OAuth2Authentication) {
                OAuth2Authentication athentication = (OAuth2Authentication) user;
                String clientId = athentication.getOAuth2Request().getClientId();
                Map map = sysClientDao.getClient(clientId);
                if (map == null) {
                    return false;
                } else {
                    List<Map> list = sysServiceDao.listByClientId(Long.valueOf(String.valueOf(map.get("id"))));
                    for (Iterator<Map> it = list.iterator(); it.hasNext(); ) {
                        Map temp = it.next();
                        if (antPathMatcher.match(String.valueOf(temp.get("path")), request.getRequestURI())) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return hasPermission;
    }

}
