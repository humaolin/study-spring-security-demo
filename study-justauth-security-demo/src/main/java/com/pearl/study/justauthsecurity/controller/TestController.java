package com.pearl.study.justauthsecurity.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping
public class TestController {

    @GetMapping("/user-info")
    public Object info() throws IOException {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
