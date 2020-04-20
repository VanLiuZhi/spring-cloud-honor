package com.vanliuzhi.org.oauth2.jwt.controller;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Author VanLiuZhi
 * @Date 2020-04-19 23:54
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("getCurrentUser")
    public Object getCurrentUser(Authentication authentication, HttpServletRequest request) {

        // return authentication.getPrincipal();

        String header = request.getHeader("Authorization");
        String token = StrUtil.subAfter(header, "bearer ", false);
        return Jwts.parser()
                .setSigningKey("test_key".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
