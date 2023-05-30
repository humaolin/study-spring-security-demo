package com.pearl.study.config.demo;

import com.pearl.study.config.demo.i18n.MessageSourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Locale;

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

    @Autowired
    MessageSourceUtils messageSourceUtils;

    @Autowired
    ReloadableResourceBundleMessageSource securityMessageSource;

    @RequestMapping(value = "/ex")
    public Object ex() {
        // 获取security 提供的多语言
        String message = securityMessageSource.getMessage("BindAuthenticator.badCredentials", null, LocaleContextHolder.getLocale());
        System.out.println(message);
        return MessageSourceUtils.getMsg("msg");
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
