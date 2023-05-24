package com.example.study.order.config;

import com.example.study.order.handler.JsonAccessDeniedHandler;
import com.example.study.order.handler.JsonAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/24
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests()
                .requestMatchers("/sms/send/Captcha", "sms/login").permitAll()
                .anyRequest().authenticated();
        // 关闭表单登录
        http.formLogin().disable();
        // 关闭 Basic认证
        http.httpBasic().disable();
        // 关闭 CSRF
        http.csrf().disable();
        // 异常配置
        http.exceptionHandling()
                .accessDeniedHandler(new JsonAccessDeniedHandler()) // 权限不足
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint()); // 未登录认证入口
        return http.build();
    }
}
