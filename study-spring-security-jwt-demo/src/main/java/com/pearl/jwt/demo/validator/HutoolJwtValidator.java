package com.pearl.jwt.demo.validator;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import com.pearl.jwt.demo.jwt.AuthenticationConstants;
import com.pearl.jwt.demo.jwt.JwtAuthenticationException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/5
 */
public class HutoolJwtValidator implements JwtValidator {

    @Override
    public String validate(String token) {
        // 验签
        boolean verify = JWTUtil.verify(token, AuthenticationConstants.JWT_KEY.getBytes());
        if (!verify) {
            throw new JwtAuthenticationException("非法令牌");
        }
        // 过期
        final JWT jwt = JWTUtil.parseToken(token);
        long expireTime = (long) jwt.getPayload("expire_time");
        if (System.currentTimeMillis() > expireTime) {
            throw new JwtAuthenticationException("令牌已失效");
        }
        // 返回
        return (String) jwt.getPayload("username");
    }
}
