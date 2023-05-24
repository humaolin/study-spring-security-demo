package com.example.study.authserver.response;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/18
 */
public class ResponseUtils {

    public static void buildResponse(HttpServletResponse response, Object result, HttpStatus httpStatus) throws IOException {
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(httpStatus.value());  // 状态码
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
