package com.example.study.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/24
 */
@RestController
public class SessionController {

    @GetMapping("/testAccess")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
    public Object testAccess() {
        return "success";
    }

    @GetMapping("/test")
    public Object test(HttpSession httpSession) {
        System.out.println("ID:" + httpSession.getId());
        System.out.println("创建时间:" + httpSession.getCreationTime());
        System.out.println("最后访问时间:" + httpSession.getLastAccessedTime());
        System.out.println("所有属性名称:" + httpSession.getAttributeNames());
        System.out.println("最大非活动间隔:" + httpSession.getMaxInactiveInterval());
        System.out.println("获取会话中的用户信息:" + httpSession.getAttribute("SPRING_SECURITY_CONTEXT"));
        return "success";
    }

    @GetMapping("/onlineCount")
    public Object onlineCount(HttpSession httpSession) {
        System.out.println("ID:" + httpSession.getId());
        System.out.println("创建时间:" + httpSession.getCreationTime());
        System.out.println("最后访问时间:" + httpSession.getLastAccessedTime());
        System.out.println("所有属性名称:" + httpSession.getAttributeNames());
        System.out.println("最大非活动间隔:" + httpSession.getMaxInactiveInterval());
        httpSession.setAttribute("Spring_Security_Context", "用户信息");
        System.out.println("获取:" + httpSession.getAttribute("Spring_Security_Context"));
        return "success";
    }

    @GetMapping("/test001")
    public Object test001(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        System.out.println("ID:" + httpSession.getId());
        System.out.println("创建时间:" + httpSession.getCreationTime());
        System.out.println("最后访问时间:" + httpSession.getLastAccessedTime());
        System.out.println("所有属性名称:" + httpSession.getAttributeNames());
        System.out.println("最大非活动间隔:" + httpSession.getMaxInactiveInterval());
        httpSession.setAttribute("Spring_Security_Context", "用户信息");
        System.out.println("获取:" + httpSession.getAttribute("Spring_Security_Context"));
        return "success";
    }
}
