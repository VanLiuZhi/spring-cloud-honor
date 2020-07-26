package com.example.demo.controller;

import com.example.demo.VO.LoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author VanLiuZhi
 * @date 2020/7/26 21:33
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public String login(@RequestBody LoginVO loginVO, HttpServletRequest httpservletrequest) {
        // 校验登陆
        if (!loginVO.getName().equals("liuzhi")){
            return "登陆失败";
        }
        HttpSession session = httpservletrequest.getSession();
        session.setAttribute("user", loginVO.getName());
        return "登陆成功";
    }

    /**
     * 登陆后获取用户信息
     * @return String
     */
    @PostMapping("/getInfo")
    public String getUserName(HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getSession().getAttributeNames().nextElement());
        httpServletRequest.getSession(false); // 获取session的时候不存在，不会创建，用于查询
        httpServletRequest.getSession().invalidate(); // session失效
        return (String) httpServletRequest.getSession().getAttribute("user");
    }

}
