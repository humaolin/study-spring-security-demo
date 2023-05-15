package com.pearl.authserver.demo.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class PearlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        sendAuthorizationResponse(request, response, authentication);
    }

    private void sendAuthorizationResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
                (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
        // 回调地址添加 code、state
        UriComponentsBuilder uriBuilder =
                UriComponentsBuilder.fromUriString(
                        Objects.requireNonNull(authorizationCodeRequestAuthentication.getRedirectUri()))
                        .queryParam("code", new Object[]{authorizationCodeRequestAuthentication.getAuthorizationCode().getTokenValue()});
        if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
            uriBuilder.queryParam("state", UriUtils.encode(authorizationCodeRequestAuthentication.getState(), StandardCharsets.UTF_8));
        }
        // 添加自定义参数
        uriBuilder.queryParam("user_id", "123456");
        String redirectUri = uriBuilder.build(true).toUriString();
        this.redirectStrategy.sendRedirect(request, response, redirectUri);
    }
}
