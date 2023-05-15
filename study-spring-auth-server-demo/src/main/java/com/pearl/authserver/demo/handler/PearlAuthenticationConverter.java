package com.pearl.authserver.demo.handler;

import com.pearl.authserver.demo.password.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class PearlAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        // 不是 GET 请求，不处理
        if ("GET".equals(request.getMethod())) {
            // 解析参数
            MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
            String targetUri = request.getParameter("target_uri");
            // 没传target_uri参数，报错
            if (!StringUtils.hasText(targetUri) && !((parameters.get("targetUri")).size() == 1)) {
                OAuth2Error error = new OAuth2Error("400", "参数[targetUri]不能为空", "");
                throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, null);
            }
        }
        return null;
    }
}
