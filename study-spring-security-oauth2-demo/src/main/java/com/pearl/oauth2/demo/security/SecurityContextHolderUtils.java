package com.pearl.oauth2.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityContextHolder工具类
 *
 * @author TangDan
 * @version 1.0
 * @since 2023/4/23
 */
public class SecurityContextHolderUtils {

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return getCurrentLoginUserInfo().getUserId();
    }

    /**
     * 获取认证对象
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录用户信息
     */
    public static PearlUserDetails getCurrentLoginUserInfo() {
        return (PearlUserDetails) getAuthentication().getPrincipal();
    }
}
