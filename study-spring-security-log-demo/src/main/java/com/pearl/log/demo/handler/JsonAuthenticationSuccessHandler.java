package com.pearl.log.demo.handler;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import com.pearl.log.demo.entity.LoginLog;
import com.pearl.log.demo.entity.PearlUserDetails;
import com.pearl.log.demo.entity.RequestDTO;
import com.pearl.log.demo.service.ILoginLogService;
import com.pearl.log.demo.util.IpUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    ILoginLogService loginLogService;

    /**
     * 登录成功后直接返回 JSON
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 成功认证的用户信息
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1. 记录登录成功日志
        RequestDTO requestDTO = loginLogService.getRequestDTO(request);
        loginLogService.save(requestDTO, true, "登录成功");
        // 2. 响应
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(HttpStatus.OK.value());  // 状态码 200
        Map<String, Object> result = new HashMap<>(); // 返回结果
        result.put("msg", "登录成功");
        result.put("code", 200);
        result.put("data", authentication);
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
