package com.pearl.security.auth.common;

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
public enum ResultCodeEnum implements StatusCode {

    FAIL(-1, "操作失败"),
    SUCCESS(200, "操作成功");

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
