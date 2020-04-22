package com.vanliuzhi.org.custom.resource.server.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description RBAC服务
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
public interface RbacService {

    /**
     * 判断是否有权限
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
