package com.example.demo.controller;

import com.example.demo.entity.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author liuzhi
 */
@RestController
public class TestController {

    @PostMapping("/test")
    public String test(User user) {
        System.out.println(user);
        String name = user.getName();
        return name;
    }
}
