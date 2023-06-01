package com.pearl.authorize.demo.controller;

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

    @GetMapping("/test001")
    public String test001(){
        return "test001";
    }

    @GetMapping("/test002")
    public String test002(){
        return "test002";
    }

}
