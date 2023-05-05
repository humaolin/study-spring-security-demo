package com.pearl.jwt.demo.config;

import com.pearl.jwt.demo.jwt.JwtTokenAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.pearl.jwt.demo.config.JwtAuthenticationConfigurer.jwtAuth;


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
        http.formLogin().successHandler(new JwtTokenAuthenticationSuccessHandler());
        http.csrf().disable();
        // JWT
        // 开启JWT（默认关闭）
        http.apply(jwtAuth());
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