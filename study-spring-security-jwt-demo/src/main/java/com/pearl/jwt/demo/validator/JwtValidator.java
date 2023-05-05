package com.pearl.jwt.demo.validator;

import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/5
 */
public interface JwtValidator {
    /**
     * 令牌校验，并返回用户唯一标识
     *
     * @param token 令牌字符串
     * @return 用户唯一标识
     */
    String validate(String token) throws IOException;
}
