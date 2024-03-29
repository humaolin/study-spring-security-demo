package com.pearl.resources.server.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/28
 */
@RequestMapping
@RestController
public class ResourceController {

    @GetMapping("/resource")
    public String resource(){
        return "访问到了resource资源 ";
    }

    @GetMapping("/userInfo")
    public String userInfo(){
        return "userInfo";
    }

    @GetMapping("/orgInfo")
    public String orgInfo(){
        return "orgInfo";
    }
}
