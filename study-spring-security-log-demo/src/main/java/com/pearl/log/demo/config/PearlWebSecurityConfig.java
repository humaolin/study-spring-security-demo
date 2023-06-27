package com.pearl.log.demo.config;

import com.pearl.log.demo.handler.JsonAuthenticationFailureHandler;
import com.pearl.log.demo.handler.JsonAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity(debug = false)
@EnableAsync
public class PearlWebSecurityConfig {

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/hello");
    }

    @Autowired
    JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler;

    @Autowired
    JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests((authorizeHttpRequests) -> {
            authorizeHttpRequests.anyRequest().authenticated();
        });
        // 开启表单登录
        http.formLogin((formLogin) -> {
            formLogin
                    .successHandler(jsonAuthenticationSuccessHandler)
                    .failureHandler(jsonAuthenticationFailureHandler);
        });
        // 关闭 CSRF
        http.csrf(AbstractHttpConfigurer::disable);
        // 开启 OAuth2 登录
        http.oauth2Login((oauth2Login) -> {
            oauth2Login.successHandler(jsonAuthenticationSuccessHandler)
                    .failureHandler(jsonAuthenticationFailureHandler);
        });
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}