package com.pearl.authserver.demo.password;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class OAuth2AuthorizationPasswordRequestAuthenticationException extends OAuth2AuthenticationException {
    private final OAuth2AuthorizationPasswordRequestAuthenticationToken authorizationCodeRequestAuthentication;

    public OAuth2AuthorizationPasswordRequestAuthenticationException(OAuth2Error error, @Nullable OAuth2AuthorizationPasswordRequestAuthenticationToken authorizationCodeRequestAuthentication) {
        super(error);
        this.authorizationCodeRequestAuthentication = authorizationCodeRequestAuthentication;
    }

    public OAuth2AuthorizationPasswordRequestAuthenticationException(OAuth2Error error, Throwable cause, @Nullable OAuth2AuthorizationPasswordRequestAuthenticationToken authorizationCodeRequestAuthentication) {
        super(error, cause);
        this.authorizationCodeRequestAuthentication = authorizationCodeRequestAuthentication;
    }

    @Nullable
    public OAuth2AuthorizationPasswordRequestAuthenticationToken getAuthorizationCodeRequestAuthentication() {
        return this.authorizationCodeRequestAuthentication;
    }
}