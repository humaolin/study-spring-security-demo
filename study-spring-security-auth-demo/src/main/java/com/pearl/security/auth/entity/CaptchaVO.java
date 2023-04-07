package com.pearl.security.auth.entity;

import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/6
 */
@Data
public class CaptchaVO implements Serializable {
    // 唯一ID
    private String id;
    // 验证码图片 Base64
    private String base64;
}
