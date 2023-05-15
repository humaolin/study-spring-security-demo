package com.pearl.authserver.demo.password;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class OAuth2AuthenticationExceptionUtils {

    public static void throwError(String errorCode, String parameterName, OAuth2AuthorizationPasswordRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient) {
        throwError(errorCode, parameterName, "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1", authorizationCodeRequestAuthentication, registeredClient, (OAuth2AuthorizationRequest)null);
    }

    public static void throwError(String errorCode, String parameterName, String errorUri, OAuth2AuthorizationPasswordRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throwError(error, parameterName, authorizationCodeRequestAuthentication, registeredClient, authorizationRequest);
    }

    public static void throwError(OAuth2Error error, String parameterName, OAuth2AuthorizationPasswordRequestAuthenticationToken authorizationCodeRequestAuthentication, RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
        throw new OAuth2AuthorizationPasswordRequestAuthenticationException(error,authorizationCodeRequestAuthentication);
    }
}
