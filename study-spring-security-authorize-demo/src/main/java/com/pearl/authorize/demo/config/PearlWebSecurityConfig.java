package com.pearl.authorize.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(securedEnabled = true)
public class PearlWebSecurityConfig {

    /**
     * 基于内存创建用户
     */
    @Bean
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        // 系统管理员
        detailsManager.createUser(User.withUsername("sysadmin").password("{noop}123456").roles("ADMIN").authorities("save").build());
        // 普通用户
        detailsManager.createUser(User.withUsername("user").password("{noop}123456").roles("USER").build());
        return detailsManager;
    }

    /**
     * 基于URL
     */
/*    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // authorizeHttpRequests：指定多个授权规则，按照顺序
        http.authorizeHttpRequests()
                // permitAll，指定放行(不需要登录)路径
                .requestMatchers("/resources/**", "/signup", "/about").permitAll()
                // 以/user/开头的请求，必须拥有 ADMIN角色
                .requestMatchers("/user/list").hasAuthority("list_user")
                // 以/db/开头的请求，必须同时拥有ADMIN、DBA角色
                .requestMatchers("/db/**").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') and hasRole('DBA')"))
                // 可以使用AuthorizationManager 添加多个规则
                // .requestMatchers("/db/**").access(AuthorizationManagers.allOf(AuthorityAuthorizationManager.hasRole("ADMIN"), AuthorityAuthorizationManager.hasRole("DBA")))
                .requestMatchers(new AntPathRequestMatcher("/test/**","POST")).hasAnyRole("ADMIN","USER")
                // 其他请求没有被以上规则匹配时，将会拒绝访问
                //.anyRequest().denyAll()
                // 其他任何请求必须认证
                .anyRequest().authenticated();
        // 开启表单登录
        http.formLogin();
        // 开启Basic认证
        http.httpBasic();
        // 关闭 CSRF
        http.csrf().disable();
        return http.build();
    }*/

    /**
     * 基于方法AOP
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // authorizeHttpRequests：指定多个授权规则，按照顺序
        http.authorizeHttpRequests()
                .anyRequest().authenticated();
        // 开启表单登录
        http.formLogin();
        // 开启Basic认证
        http.httpBasic();
        // 关闭 CSRF
        http.csrf().disable();
        return http.build();
    }
}