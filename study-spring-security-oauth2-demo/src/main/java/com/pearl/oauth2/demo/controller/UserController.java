
package com.pearl.oauth2.demo.controller;


import com.pearl.oauth2.demo.security.SecurityContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
public class UserController {

    // 默认重定向URI模板是 {baseUrl}/login/oauth2/code/{registrationId}。registrationId 是 ClientRegistration 的唯一标识符。
    @GetMapping("/user-info")
    @ResponseBody
    Object userInfo() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

/*    @GetMapping("/user-info")
    @ResponseBody
    Object userInfo() {

        return SecurityContextHolderUtils.getCurrentLoginUserInfo();
    }*/
}
