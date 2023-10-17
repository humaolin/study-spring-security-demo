
package com.pearl.authorize.redis.demo;


import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/save")
    String save() {
        return "save";
    }

    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/list")
    String list() {
        return "list";
    }

}
