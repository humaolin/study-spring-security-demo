package com.pearl.separation.demo.handler;

import com.pearl.separation.demo.response.R;
import com.pearl.separation.demo.response.ResponseUtils;
import com.pearl.separation.demo.response.ResultCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/29
 */
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * @param request        请求
     * @param response       响应
     * @param authentication 成功认证的用户信息
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseUtils.buildResponse(response, R.response(ResultCodeEnum.AUTHENTICATION_SUCCESS, authentication), HttpStatus.OK);
    }
}
