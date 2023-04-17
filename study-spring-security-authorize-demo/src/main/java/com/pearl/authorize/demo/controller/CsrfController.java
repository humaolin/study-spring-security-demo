
package com.pearl.authorize.demo.controller;


import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pearl
 * @since 2023-03-23
 */
@Slf4j
@RestController
public class CsrfController {

    @PostMapping("/transfer")
    public String permit(String name, HttpServletRequest request) {
        // 1. 模拟存储中查找令牌
        String saveToken = "4bfd1575-3ad1-4d21-96c7-4ef2d9f86721";
        // 2. 解析请求消息头中的令牌
        String csrfToken = request.getHeader("csrfToken");
        // 3. 没有令牌、令牌不匹配时访问被拒绝
        if (StrUtil.isEmpty(csrfToken) || !saveToken.equals(csrfToken)) {
            throw new AccessDeniedException("访问被拒绝");
        }
        log.info("给" + name + "转账[1000000]越南盾");
        return "转账成功";
    }
}
