package com.pearl.authorize.redis.demo.exception;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/18
 */
public class ResponseUtils {

    public static void buildResponse(HttpServletResponse response, Exception exception, String msg,HttpStatus httpStatus) throws IOException {
        exception.printStackTrace(); // 打印异常
        response.setContentType("application/json;charset=utf-8"); // 返回JSON
        response.setStatus(httpStatus.value());  // 状态码
        Map<String, Object> result = new HashMap<>(); // 返回结果
        result.put("code", httpStatus.value());
        result.put("msg", msg);
        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
