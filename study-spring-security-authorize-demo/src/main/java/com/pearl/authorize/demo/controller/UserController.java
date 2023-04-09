package com.pearl.authorize.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pearl
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/permit")
    String permit() {
        return "permit";
    }

    //@PreAuthorize("authentication.name ='root'")
    //@PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
    @PreAuthorize("authentication.name ==#name")
    @GetMapping("/save")
    String save(String name) {
        return "save";
    }

    @GetMapping("/del")
    String del() {
        return "del";
    }

    @GetMapping("/update")
    String update() {
        return "update";
    }

    @GetMapping("/list")
    String list() {
        return "list";
    }

}
