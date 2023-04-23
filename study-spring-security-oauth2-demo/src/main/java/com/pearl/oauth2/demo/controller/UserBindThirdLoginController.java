package com.pearl.oauth2.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pearl
 * @since 2023-04-23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/userBindThirdLogin")
public class UserBindThirdLoginController {

    private final InMemoryClientRegistrationRepository clientRegistrationRepository;

    public Object queryUserUnbind() {
        // 1. 登录
        // 1.1 Oauth2登录 发现uid有登录用户，uid查询用户信息
        // 1.2 Oauth2登录，没有绑定用户，弹出注册页面

        // 2. 登录后
        // 2.1 绑定，发起授权、登录、回调接收用户信息进行绑定
        // 2.2 解绑 直接删除
        return null;
    }

    public Object queryUserBind() {
        return null;
    }

    private Object selectUserUnbind() {
        for (ClientRegistration client : clientRegistrationRepository) {
            String appId = client.getRegistrationId();
        }
        return null;
    }
}
