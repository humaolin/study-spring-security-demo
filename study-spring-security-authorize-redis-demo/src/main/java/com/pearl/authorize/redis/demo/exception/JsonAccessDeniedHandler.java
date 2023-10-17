package com.pearl.authorize.redis.demo.exception;

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
 * @since 2023/4/18
 */
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        ResponseUtils.buildResponse(response,exception,"权限不足，访问被拒绝",HttpStatus.FORBIDDEN);
    }
}
