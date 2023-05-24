package com.example.study.authserver.handler;

import cn.hutool.json.JSONUtil;
import com.example.study.authserver.response.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/18
 */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(HttpStatus.UNAUTHORIZED.value());  // 状态码
        Map<String, Object> result = new HashMap<>(); // 返回结果
        result.put("msg", "未登录");
        result.put("code", 401);
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
