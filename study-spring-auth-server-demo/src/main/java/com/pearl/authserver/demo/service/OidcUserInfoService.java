package com.pearl.authserver.demo.service;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Set;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/10
 */
public interface OidcUserInfoService {
    OidcUserInfo loadUser(String username, Set<String> scopes);
}
