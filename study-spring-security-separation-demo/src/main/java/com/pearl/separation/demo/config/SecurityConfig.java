package com.pearl.separation.demo.config;

import com.pearl.separation.demo.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    /**
     *  前后端分离
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests()
                .anyRequest().authenticated();
        // 开启表单登录
        http.formLogin()
                .successHandler(new JsonAuthenticationSuccessHandler()) // 登录成功处理器
                .failureHandler(new JsonAuthenticationFailureHandler());// 登录失败处理器
        // 关闭 CSRF
        // 异常配置
        http.exceptionHandling()
                .accessDeniedHandler(new JsonAccessDeniedHandler()) // 权限不足
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint()); // 认证入口
        // 注销登录
        http.logout()
                .logoutSuccessHandler(new JsonLogoutSuccessHandler()); //  自定义注销成功处理器
        http.csrf().disable();
        return http.build();
    }
    /**
     * 内存存储用户
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("123456")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
}