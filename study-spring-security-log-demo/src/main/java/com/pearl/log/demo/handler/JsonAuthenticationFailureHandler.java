package com.pearl.log.demo.handler;

import cn.hutool.json.JSONUtil;
import com.pearl.log.demo.entity.LoginLog;
import com.pearl.log.demo.entity.RequestDTO;
import com.pearl.log.demo.service.ILoginLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/29
 */
@Component
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    ILoginLogService loginLogService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 1. 记录登录失败日志
        RequestDTO requestDTO = loginLogService.getRequestDTO(request);
        loginLogService.save(requestDTO, false, "登录失败：" + exception.getMessage());
        // 2. 响应
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(HttpStatus.UNAUTHORIZED.value());  // 状态码 400
        Map<String, Object> result = new HashMap<>(); // 返回结果
        result.put("code", 401);
        result.put("data", exception.getMessage());
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
