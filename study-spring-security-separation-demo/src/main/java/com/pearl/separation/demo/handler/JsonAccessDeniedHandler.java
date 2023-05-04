package com.pearl.separation.demo.handler;

import com.pearl.separation.demo.response.R;
import com.pearl.separation.demo.response.ResponseUtils;
import com.pearl.separation.demo.response.ResultCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/4
 */
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        ResponseUtils.buildResponse(response, R.response(ResultCodeEnum.ACCESS_DENIED, null), HttpStatus.FORBIDDEN);
    }
}
