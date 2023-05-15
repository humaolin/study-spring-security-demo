package com.pearl.authserver.demo.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class TokenEndpointAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter=new OAuth2AccessTokenResponseHttpMessageConverter();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        sendAccessTokenResponse(request, response, authentication);
    }

    private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken)authentication;
        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken(); // 访问令牌对象
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken(); // 刷新令牌对象
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters(); // 额外参数
        // 构建响应对象
        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse
                .withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType())
                .scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters); // // 额外参数，比如 id_token
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        // 响应写出
        this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
    }
}
