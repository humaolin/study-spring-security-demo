package com.pearl.security.auth.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.UUID;
import com.pearl.security.auth.common.R;
import com.pearl.security.auth.entity.CaptchaVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pearl
 * @since 2023-03-23
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/generateCaptcha")
    @ResponseBody
    public R<CaptchaVO> getCaptcha(HttpServletResponse response) {
        // 定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        String code = lineCaptcha.getCode();// 验证码
        log.info("生成验证码：" + lineCaptcha.getCode());
        String imageBase64 = lineCaptcha.getImageBase64Data(); // 验证码图片BASE64
        // 创建验证码对象
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setId(UUID.randomUUID().toString());
        captchaVO.setBase64(imageBase64);
        // 缓存验证码，10分钟有效
        stringRedisTemplate.opsForValue().set(captchaVO.getId(), code, 10, TimeUnit.MINUTES);
        return R.response(200, "生成验证码成功", captchaVO);
    }
}
