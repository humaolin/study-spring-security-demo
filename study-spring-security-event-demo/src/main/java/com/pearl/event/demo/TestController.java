package com.pearl.event.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/23
 */
@RestController
public class TestController {

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Object test() {
        return "Hello Security";
    }

    @GetMapping("/test001")
    //
    //
    // 过去,Linux主要被用作,因为它的廉价、灵活性及Unix背
    public Object test001(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("AA","test");
        return "Hello Security";
    }
}
