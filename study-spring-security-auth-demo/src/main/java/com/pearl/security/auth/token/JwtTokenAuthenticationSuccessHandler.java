package com.pearl.security.auth.token;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/29
 */
public class JwtTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 登录成功后直接返回 JWT
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 成功认证的用户信息
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(HttpStatus.OK.value());  // 状态码 200
        Map<String, Object> result = new HashMap<>(); // 返回结果
        // 响应数据
        result.put("msg", "登录成功");
        result.put("code", 200);
        // 封装为JWT
        Map<String, Object> jwtMap = new HashMap<>();
        jwtMap.put("username", authentication.getName()); // 用户名
        jwtMap.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60); // 过期时间，一个小时后过期
        jwtMap.put("authentication", authentication); // 认证信息
        String token = JWTUtil.createToken(jwtMap, AuthenticationConstants.JWT_KEY.getBytes());
        result.put("data", token);
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
