package com.pearl.authserver.demo.password;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
@Getter
@Setter
public class OAuth2AuthorizationResourceOwnerPasswordRequestToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private String username;

    private String password;

    private Set<String> scopes;

    public OAuth2AuthorizationResourceOwnerPasswordRequestToken(@Nullable String username,
                                                                @Nullable String password,
                                                                @Nullable Set<String> scopes,
                                                                Authentication clientPrincipal,
                                                                @Nullable Map<String, Object> additionalParameters) {
        super(AuthorizationGrantType.PASSWORD, clientPrincipal, additionalParameters);
        Assert.hasText(username, "username cannot be empty");
        Assert.hasText(username, "password cannot be empty");
        this.username = username;
        this.password = password;
        this.scopes = scopes;
    }

    protected OAuth2AuthorizationResourceOwnerPasswordRequestToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
    }
}