package com.pearl.security.auth.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/6
 */
@Data
public class SmsCaptchaVO implements Serializable {
    // 手机号
    private String phone;
    // 多少分钟后过期
    private Integer expire;
}
