package com.pearl.security.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/6
 */
public class CaptchaVerifyException extends AuthenticationException {

    public CaptchaVerifyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaVerifyException(String msg) {
        super(msg);
    }
}
