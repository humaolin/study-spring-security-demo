package com.pearl.authserver.demo.password;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class OAuth2AuthorizationResourceOwnerPasswordException extends OAuth2AuthenticationException {
    private final OAuth2AuthorizationResourceOwnerPasswordRequestToken oAuth2AuthorizationResourceOwnerPasswordRequestToken;

    public OAuth2AuthorizationResourceOwnerPasswordException(OAuth2Error error, @Nullable OAuth2AuthorizationResourceOwnerPasswordRequestToken authorizationCodeRequestAuthentication) {
        super(error);
        this.oAuth2AuthorizationResourceOwnerPasswordRequestToken = authorizationCodeRequestAuthentication;
    }

    public OAuth2AuthorizationResourceOwnerPasswordException(OAuth2Error error, Throwable cause, @Nullable OAuth2AuthorizationResourceOwnerPasswordRequestToken authorizationCodeRequestAuthentication) {
        super(error, cause);
        this.oAuth2AuthorizationResourceOwnerPasswordRequestToken = authorizationCodeRequestAuthentication;
    }

    @Nullable
    public OAuth2AuthorizationResourceOwnerPasswordRequestToken getAuthorizationCodeRequestAuthentication() {
        return this.oAuth2AuthorizationResourceOwnerPasswordRequestToken;
    }
}