package com.pearl.event.demo;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/25
 */
@Component
public class AuthorizationEvents {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent failure) {
        Object details = failure.getAuthentication().get().getName();
        System.out.println("权限不足：" + details);
    }
}
