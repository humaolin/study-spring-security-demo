package com.pearl.event.demo;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.event.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/21
 *
 */
@Component
public class AuthenticationEvents {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        Object name = success.getAuthentication().getName();
        System.out.println("登录成功：" + name);
        // ...
    }

/*
    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        Object name = event.getAuthentication().getName();
        System.out.println("登录失败：" + name + "原因：" + event.getException().getMessage());
    }
*/

    /**
     * BadCredentialsException
     * UsernameNotFoundException
     * InvalidBearerTokenException
     */
    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        Object name = event.getAuthentication().getName();
        System.out.println("登录失败：" + name + "原因：" + event.getException().getMessage());
    }

    @EventListener
    public void onFailure(AuthenticationFailureExpiredEvent event) {
        Object name = event.getAuthentication().getName();
        System.out.println("账号过期：" + name);
    }

    @EventListener
    public void onFailure(AuthenticationFailureProviderNotFoundEvent event) {
        Object name = event.getAuthentication().getName();
        System.out.println("未找到匹配的认证程序：" + name);
    }

    @EventListener
    public void onFailure(AuthenticationFailureDisabledEvent event) {
        Object name = event.getAuthentication().getName();
        System.out.println("帐户被禁用：" + name);
    }

    @EventListener
    public void onFailure(AuthenticationFailureLockedEvent event) {
        Object name = event.getAuthentication().getName();
        System.out.println("帐户被锁定：" + name);
    }

    @EventListener
    public void onFailure(AuthenticationFailureServiceExceptionEvent event) {
        Object name = event.getAuthentication().getName();
        System.out.println("认证管理器AuthenticationManager内部存在问题：" + name);
    }


    @EventListener
    public void onFailure(AuthenticationFailureCredentialsExpiredEvent event) {
        Object name = event.getAuthentication().getName();
        System.out.println("密码过期：" + name);
    }
}

