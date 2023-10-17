package com.pearl.event.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.Executor;

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
    public Object test001(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("AA", "test");
        return "Hello Security";
    }

    @GetMapping("/runnable")
    public Object runnable() {
        System.out.println("线程：" + Thread.currentThread().getName());
        System.out.println("登录用户：" + SecurityContextHolder.getContext().getAuthentication().getName());
        new Thread(() -> {
            System.out.println("线程：" + Thread.currentThread().getName());
            System.out.println("登录用户：" + SecurityContextHolder.getContext().getAuthentication().getName());
        }).start();

        // 方式 1
        DelegatingSecurityContextRunnable delegatingSecurityContextRunnable = new DelegatingSecurityContextRunnable(() -> {
            System.out.println("线程：" + Thread.currentThread().getName());
            System.out.println("登录用户：" + SecurityContextHolder.getContext().getAuthentication().getName());
        });
        new Thread(delegatingSecurityContextRunnable).start();

        // 线程池
        SimpleAsyncTaskExecutor delegateExecutor = new SimpleAsyncTaskExecutor();
        DelegatingSecurityContextExecutor executor = new DelegatingSecurityContextExecutor(delegateExecutor, SecurityContextHolder.getContext());
        Runnable originalRunnable = () -> {
            System.out.println("线程：" + Thread.currentThread().getName());
            System.out.println("登录用户：" + SecurityContextHolder.getContext().getAuthentication().getName());
        };
        executor.execute(originalRunnable);

        submitRunnable();
        return "Hello Security";
    }

    @Autowired
    private Executor executor; // becomes an instance of our DelegatingSecurityContextExecutor

    public void submitRunnable() {
        Runnable originalRunnable = () -> {
            System.out.println("线程：" + Thread.currentThread().getName());
            System.out.println("登录用户：" + SecurityContextHolder.getContext().getAuthentication().getName());
        };
        executor.execute(originalRunnable);
    }


}
