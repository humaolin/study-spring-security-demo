package com.pearl.log.demo.entity;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.Header;
import com.pearl.log.demo.util.IpUtils;
import lombok.Data;

import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/28
 */
@Data
public class RequestDTO {
    // 请求URI
    String requestUri;
    // UA
    String userAgent;
    // 客户端IP地址
    String ipAddress;
    // 请求参数
    Map<String, String> paramMap;
}
