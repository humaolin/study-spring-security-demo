package com.pearl.event.demo;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;
import java.util.regex.Pattern;

@Configuration
public class SecurityConfig {

    /**
     * 认证事件发布器
     */
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    /**
     * 授权事件发布
     */
    @Bean
    public AuthorizationEventPublisher authorizationEventPublisher
            (ApplicationEventPublisher applicationEventPublisher) {
        return new SpringAuthorizationEventPublisher(applicationEventPublisher);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(e -> {
            e.anyRequest().authenticated();
        });
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

    /**
     * 防火墙配置
     */
/*    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        // 只允许GET POST请求
        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST"));
        // 允许所有方法（不推荐）
        //firewall.setUnsafeAllowAnyHttpMethod(true);

        // 2. URL规范
        // 是否允许分号（;、%3b、%3B）
        firewall.setAllowSemicolon(true);
        // 是否允许斜杠（%2f、%2F）
        firewall.setAllowUrlEncodedSlash(true);
        // 是否允许反斜杠（\\、%5c、%5C）
        firewall.setAllowBackSlash(true);
        // 是否允许百分号%（%、%25）
        firewall.setAllowUrlEncodedPercent(true);
        // 是否允许句号. （%2e、%2E）
        firewall.setAllowUrlEncodedPeriod(true);

        // Header
        // 允许的Header名称
        firewall.setAllowedHeaderNames((allowedHeaderNames) -> allowedHeaderNames.equalsIgnoreCase("Token"));
        // 允许的Header值
        firewall.setAllowedHeaderValues((allowedHeaderValues) -> allowedHeaderValues.equalsIgnoreCase("AAA"));

        // 参数
        firewall.setAllowedParameterNames(allowedParameterNames-> allowedParameterNames.equalsIgnoreCase("AA"));
        firewall.setAllowedParameterValues(allowedParameterNames-> allowedParameterNames.equalsIgnoreCase("BB"));


        // 主机名
        firewall.setAllowedHostnames((hostname) ->
                hostname.equalsIgnoreCase("localhost"));
        return firewall;
    }*/

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