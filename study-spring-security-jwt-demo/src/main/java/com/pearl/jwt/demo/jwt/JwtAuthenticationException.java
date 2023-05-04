package com.pearl.jwt.demo.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/4
 */
public class JwtAuthenticationException extends AuthenticationException {


    public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
