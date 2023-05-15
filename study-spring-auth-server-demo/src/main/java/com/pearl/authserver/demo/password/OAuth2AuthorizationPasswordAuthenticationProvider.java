package com.pearl.authserver.demo.password;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
@AllArgsConstructor
public class OAuth2AuthorizationPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2AuthorizationPasswordRequestAuthenticationToken passwordAthenticationToken =
                (OAuth2AuthorizationPasswordRequestAuthenticationToken) authentication;
        // 1.
        OAuth2ClientAuthenticationToken clientPrincipal =
                OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(passwordAthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
            OAuth2AuthenticationExceptionUtils.throwError("","",null,null);
        }
        // 2.
        String username = passwordAthenticationToken.getUsername();
        String password = passwordAthenticationToken.getPassword();

        // 3.
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (ObjectUtil.isNull(userDetails)){
            OAuth2AuthenticationExceptionUtils.throwError("","",null,null);
        }
        // 密码校验


        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(username)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD);
        // 令牌上下文
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(passwordAthenticationToken.getScopes())
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrant(passwordAthenticationToken);
        // 生成访问令牌
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2AuthenticationExceptionUtils.throwError("","",null,null);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }
        // 生成刷新令牌
        OAuth2RefreshToken refreshToken = null;
/*        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", "ERROR_URI");
                OAuth2AuthenticationExceptionUtils.throwError(error,"null",null,null,null);
            }
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }*/
        Map<String, Object> additionalParameters = Collections.emptyMap();
        // 返回 OAuth2AccessTokenAuthenticationToken 令牌对象
        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
    }





    /**
     * 支持验证的类型
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthorizationPasswordRequestAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
