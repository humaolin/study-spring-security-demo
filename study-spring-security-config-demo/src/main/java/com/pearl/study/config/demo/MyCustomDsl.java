package com.pearl.study.config.demo;


import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/29
 */
public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    private boolean flag;

    /**
     * 初始化方法，用于对其他配置进行设置
     */
    @Override
    public void init(HttpSecurity http) throws Exception {
        http.csrf(
                csrf->{
                    csrf.disable();
                }
        );
    }

    /**
     * 添加当前功能配置，一般是添加过滤器并进行初始化配置
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 获取Spring 容器
        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        // 获取Bean 示例
        UserDetailsService userDetailsService = context.getBean(UserDetailsService.class);
        Assert.notNull(userDetailsService,"UserDetailsService 不能为空");
        // 设置到过滤器中
        MyFilter myFilter = new MyFilter();
        http.addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class);
    }

    public MyCustomDsl flag(boolean value) {
        this.flag = value;
        return this;
    }

    public static MyCustomDsl customDsl() {
        return new MyCustomDsl();
    }
}