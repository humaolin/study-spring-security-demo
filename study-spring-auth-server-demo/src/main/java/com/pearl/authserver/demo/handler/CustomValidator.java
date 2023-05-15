package com.pearl.authserver.demo.handler;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class CustomValidator implements Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> {

    @Override
    public void accept(OAuth2AuthorizationCodeRequestAuthenticationContext authenticationContext) {
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
                authenticationContext.getAuthentication();
        Map<String, Object> parameters = authorizationCodeRequestAuthentication.getAdditionalParameters();
        String targetUri = (String) parameters.get("target_uri");
        // 没传target_uri参数，报错
        if (!StringUtils.hasText(targetUri)) {
            OAuth2Error error = new OAuth2Error("400", "参数[targetUri]不能为空", "");
            throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, null);
        }
    }
}