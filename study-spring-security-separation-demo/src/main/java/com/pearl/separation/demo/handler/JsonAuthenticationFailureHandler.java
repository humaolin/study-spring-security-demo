package com.pearl.separation.demo.handler;

import cn.hutool.json.JSONUtil;
import com.pearl.separation.demo.response.R;
import com.pearl.separation.demo.response.ResponseUtils;
import com.pearl.separation.demo.response.ResultCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/29
 */
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResponseUtils.buildResponse(response, R.response(HttpStatus.UNAUTHORIZED.value(), exception.getLocalizedMessage(), null), HttpStatus.UNAUTHORIZED);
    }
}
