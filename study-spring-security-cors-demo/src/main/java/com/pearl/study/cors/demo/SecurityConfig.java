package com.pearl.study.cors.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    /**
     * 前后端分离
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests((authorize) -> authorize
                .anyRequest().authenticated()
        );
        // if Spring MVC is on classpath and no CorsConfigurationSource is provided,
        // Spring Security will use CORS configuration provided to Spring MVC
        // 如果Spring MVC在类路径上，并且没有提供CorsConfigurationSource，则Spring Security将使用提供给Spring MVC的CORS配置
        http.cors(withDefaults());
        // 开启表单登录
        http.formLogin();
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