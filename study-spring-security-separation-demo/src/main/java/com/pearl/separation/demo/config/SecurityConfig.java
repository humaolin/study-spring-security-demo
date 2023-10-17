package com.pearl.separation.demo.config;

import com.pearl.separation.demo.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    /**
     * 前后端分离
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests()
                .requestMatchers("/authentication").hasAnyAuthority("ROLE_ANO", "ROLE_USER")
                .anyRequest().authenticated();
        // 关闭匿名认证
        // http.anonymous(AbstractHttpConfigurer::disable);
        // 匿名认证配置
        http.anonymous(anonymous -> {
            anonymous.key("123456") // 用于识别此对象是否由授权客户制作的密钥，会计算哈希值并存放在匿名认证信息中，可以取出后和当前配置项进行比较，判断是否由当前服务产生的匿名用户
                    .principal("ANO") // 设置匿名用户用户名
                    .authorities("ROLE_ANO"); // 设置匿名用户角色权限值
        });
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
        //
        http.x509(x509 -> {
        });
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