package com.pearl.security.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/27
 */
public class MyOncePerRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("---------进入MyOncePerRequestFilter前置------------");
        filterChain.doFilter(request, response);
        System.out.println("------------进入MyOncePerRequestFilter后置--------");
    }
}
