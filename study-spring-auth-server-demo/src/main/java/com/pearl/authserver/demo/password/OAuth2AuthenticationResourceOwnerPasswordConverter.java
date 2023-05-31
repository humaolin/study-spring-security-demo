package com.pearl.authserver.demo.password;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
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
public class OAuth2AuthenticationResourceOwnerPasswordConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        // 1. 不是密码模式，直接返回NULL
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
            return null;
        }
        // 2. 客户端认证是否已通过
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (clientPrincipal == null || !clientPrincipal.isAuthenticated()) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, null, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request); // 获取所有请求参数
        // 3. 授权范围必填
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (!StringUtils.hasText(scope) || parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        Set<String> scopes = new HashSet<>(StrUtil.split(scope, " "));
        // 4. 用户名username必填
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) ||
                parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.USERNAME, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        // 5. 密码password必填
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) ||
                parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.PASSWORD, OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        // 6. 其他参数
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.SCOPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_SECRET) &&
                    !key.equals(OAuth2ParameterNames.PASSWORD) &&
                    !key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.USERNAME)) {
                additionalParameters.put(key, value.get(0));
            }

        });
        // 7. 返回
        return new OAuth2AuthorizationResourceOwnerPasswordRequestToken(username, password, scopes, clientPrincipal, additionalParameters);
    }
}
