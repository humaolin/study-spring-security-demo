package com.pearl.separation.demo.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class AnonymousController {

    @GetMapping("/test")
    Object test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getName();  // 获取用户名
        AuthenticationTrustResolverImpl resolver = new AuthenticationTrustResolverImpl();
        boolean isAnonymous = resolver.isAnonymous(authentication);
        System.out.println("isAnonymous:" + isAnonymous);
        return "";
    }

    @GetMapping("/context")
    public String method(@CurrentSecurityContext SecurityContext context) {
        return context.getAuthentication().getName();
    }

    @GetMapping("/name")
    public String name(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        return authentication.getName();
    }

    @GetMapping("/authentication")
    public String method(Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "anonymous";
        } else {
            return "not anonymous";
        }
    }


    @GetMapping("/principal")
    public String principal(@AuthenticationPrincipal UserDetails user) {
       return user.getUsername();
    }

}
