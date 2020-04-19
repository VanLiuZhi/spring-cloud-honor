package com.vanliuzhi.org.oauth2.simple.controller;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:54
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("getCurrentUser")
    public Object getCurrentUser(Authentication authentication) {

        return authentication.getPrincipal();

    }
}
