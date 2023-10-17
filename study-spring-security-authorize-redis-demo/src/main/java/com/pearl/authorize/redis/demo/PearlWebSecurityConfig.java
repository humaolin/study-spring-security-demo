package com.pearl.authorize.redis.demo;

import com.pearl.authorize.redis.demo.authorization.CustomMethodSecurityExpressionHandler;
import com.pearl.authorize.redis.demo.exception.JsonAccessDeniedHandler;
import com.pearl.authorize.redis.demo.exception.JsonAuthenticationEntryPoint;
import com.pearl.authorize.redis.demo.exception.JsonAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class PearlWebSecurityConfig {

    /**
     * 基于内存创建用户
     * 使用数据库时，也不需要查询用户的角色权限
     */
    @Bean
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        // 系统管理员
        detailsManager.createUser(User.withUsername("sysadmin").password("{noop}123456").build());
        // 普通用户
        detailsManager.createUser(User.withUsername("user").password("{noop}123456").build());
        return detailsManager;
    }

    @Bean
    protected MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler();
    }

    /**
     * 基于方法AOP
     * 1. 内存查询授权信息，而不是Security Context
     * 方案1：原生注解，修改从缓存中查
     * 方案2：自定义 AuthorizationManager
     * 方案3：自定义注解 + AOP
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 授权规则
        http.authorizeHttpRequests(e -> e.anyRequest().authenticated());
        // 开启表单登录
        http.formLogin(e -> e.failureHandler(new JsonAuthenticationFailureHandler()));
        // 关闭 CSRF
        http.csrf(AbstractHttpConfigurer::disable);
        // 异常配置
        http.exceptionHandling(e -> e
                .accessDeniedHandler(new JsonAccessDeniedHandler())
               /* .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))*/);
        return http.build();
    }

}