package com.pearl.separation.demo.response;

import com.pearl.separation.demo.handler.JsonAccessDeniedHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 响应状态码枚举
 *
 * @author TangDan
 * @version 1.0
 * @since 2022/9/5
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ResultCodeEnum {
    FAIL(-1, "操作失败"),
    SUCCESS(200, "操作成功"),

    LOGOUT_SUCCESS(200, "注销成功"),
    AUTHENTICATION_SUCCESS(200, "登录成功"),
    NOT_AUTHENTICATION(401, "未认证请求"),
    ACCESS_DENIED(403, "权限不足，访问被拒绝");

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
