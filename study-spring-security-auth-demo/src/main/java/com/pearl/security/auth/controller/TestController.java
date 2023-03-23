package com.pearl.security.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/23
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public Object test() {
        return "Hello Security";
    }
}
