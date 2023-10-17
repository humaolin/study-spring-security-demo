package com.pearl.separation.demo.handler;

import com.pearl.separation.demo.response.R;
import com.pearl.separation.demo.response.ResponseUtils;
import com.pearl.separation.demo.response.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 *
 * @author TangDan
 * @version 1.0
 * @since 2023/5/4
 */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseUtils.buildResponse(response, R.response(ResultCodeEnum.NOT_AUTHENTICATION,null), HttpStatus.UNAUTHORIZED);
    }
}

