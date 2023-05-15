package com.pearl.authserver.demo.password;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class OAuth2AuthenticationPasswordConverter implements AuthenticationConverter {

    private final OAuth2AuthorizationResponseType responseType = new OAuth2AuthorizationResponseType(AuthorizationGrantType.PASSWORD.getValue());

    @Override
    public Authentication convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        // 1. 授权类型必填，且为 password
        String responseType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (StringUtils.hasText(responseType) && (parameters.get(OAuth2ParameterNames.GRANT_TYPE)).size() == 1) {
            if (!responseType.equals(AuthorizationGrantType.PASSWORD.getValue())) {
                OAuth2AuthenticationExceptionUtils.throwError("","",null,null);
            }
        } else {
            OAuth2AuthenticationExceptionUtils.throwError("","",null,null);
        }
        //
        // 用户名username必填
        String username = request.getParameter(OAuth2ParameterNames.USERNAME);
        // 密码password必填
        String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
        // 客户端认证是否已通过
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null) {
            OAuth2AuthenticationExceptionUtils.throwError("","",null,null);

        }
        if (!clientPrincipal.isAuthenticated()) {
            OAuth2AuthenticationExceptionUtils.throwError("","",null,null);

        }
        Set<String> scopes = null;
        String scope = (String) parameters.getFirst("scope");
        if (StringUtils.hasText(scope) && ((List) parameters.get("scope")).size() != 1) {
            OAuth2AuthenticationExceptionUtils.throwError("","",null,null);

        }

        if (StringUtils.hasText(scope)) {
            scopes = new HashSet(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        String state = (String) parameters.getFirst("state");
        if (StringUtils.hasText(state) && ((List) parameters.get("state")).size() != 1) {
            OAuth2AuthenticationExceptionUtils.throwError("","",null,null);
        }

        Map<String, Object> additionalParameters = new HashMap();
        parameters.forEach((key, value) -> {
            if (!key.equals("response_type") && !key.equals("client_id") && !key.equals("redirect_uri") && !key.equals("scope") && !key.equals("state")) {
                additionalParameters.put(key, value.get(0));
            }

        });
        return new OAuth2AuthorizationPasswordRequestAuthenticationToken(username, password, scopes,clientPrincipal, additionalParameters);
    }
}
