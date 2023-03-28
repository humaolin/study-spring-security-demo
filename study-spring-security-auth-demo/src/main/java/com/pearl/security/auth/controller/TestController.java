package com.pearl.security.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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

    @GetMapping("/user-info")
    public Object userinfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        Object principal = authentication.getPrincipal();
        return authentication;
    }

    @ResponseBody
    @GetMapping("/async")
    public Callable<String> helloGet() throws Exception {
        System.out.println(Thread.currentThread().getName() + " 主线程start");
        Callable<String> callable = () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            System.out.println(Thread.currentThread().getName() + " 子子子线程start");
            TimeUnit.SECONDS.sleep(5); //模拟处理业务逻辑，话费了5秒钟
            System.out.println(Thread.currentThread().getName() + " 子子子线程end");
            // 这里稍微小细节一下：最终返回的不是Callable对象，而是它里面的内容
            return "hello world";
        };
        System.out.println(Thread.currentThread().getName() + " 主线程end");
        return callable;
    }
}
