package com.example.study.authserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/24
 */
@RestController
public class SessionController {

    @GetMapping("/test")
    public Object test() {
        return "success";
    }
}
