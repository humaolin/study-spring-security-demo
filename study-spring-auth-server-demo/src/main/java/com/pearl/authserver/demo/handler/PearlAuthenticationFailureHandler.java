package com.pearl.authserver.demo.handler;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/15
 */
public class PearlAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        sendErrorResponse(request, response, exception);
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        OAuth2AuthorizationCodeRequestAuthenticationException authorizationCodeRequestAuthenticationException =
                (OAuth2AuthorizationCodeRequestAuthenticationException) exception;
        OAuth2Error error = authorizationCodeRequestAuthenticationException.getError();
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication = authorizationCodeRequestAuthenticationException.getAuthorizationCodeRequestAuthentication();
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(HttpStatus.BAD_REQUEST.value());  // 状态码
        Map<String, Object> result = new HashMap<>(); // 返回结果
        result.put("msg", error.getDescription());
        result.put("code", 400);
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
