package com.pearl.study.config.demo;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/25
 */
// 使用@ServletComponentScan扫描该过滤器
//@WebFilter(filterName = "myFilter", urlPatterns = {"/*"})
@Component("myFilter")
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("---------进入MyFilter前置------------");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("------------进入MyFilter后置--------");
    }
}
