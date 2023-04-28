/*
package com.pearl.resources.server.demo.config;

*/
/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/25
 *//*


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.web.HeaderBearerTokenResolver;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


public class SpringSecurityConfig {

    */
/**
     * Spring Security SecurityFilterChain 认证配置
     *
     *//*

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {

                .formLogin().disable();
        // 资源服务器
        http.oauth2ResourceServer();
        return http.build();
    }

    */
/**
     * 内存存储用户
     *//*

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

*/
