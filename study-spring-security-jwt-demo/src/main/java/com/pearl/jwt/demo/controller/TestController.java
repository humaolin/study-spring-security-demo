package com.pearl.jwt.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/23
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public Object test() {
        return "Hello Security";
    }

    @GetMapping("/user-info")
    public Object userinfo() {
        SecurityContext context = SecurityContextHolder.getContext();// 获取 SecurityContext
        Authentication authentication = context.getAuthentication();// 获取认证信息
        String username = authentication.getName(); // 用户名
        Object principal = authentication.getPrincipal(); // 用户信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();// 权限
        return authentication;
    }
}
