package com.pearl.log.demo.util;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/26
 */
public class IpUtils {

    /**
     * 获取客户端IP
     *
     * @param request 请求对象
     * @return IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        // JDK 17：JakartaServletUtil
        // JDK 8：ServletUtil
        String clientIP = JakartaServletUtil.getClientIP(request);
        return "0:0:0:0:0:0:0:1".equals(clientIP) ? "127.0.0.1" : clientIP;
    }
}
