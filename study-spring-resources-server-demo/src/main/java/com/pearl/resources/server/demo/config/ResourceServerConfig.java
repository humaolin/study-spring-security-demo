package com.pearl.resources.server.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 *
 *
 *
 * @author TangDan
 * @version 1.0
 * @since 2023/4/28
 */
@Configuration
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/userInfo").hasAuthority("SCOPE_user_info")
                .requestMatchers("/orgInfo").hasAuthority("SCOPE_org_info")
                .requestMatchers("/resource").hasAuthority("SCOPE_resource")
                .anyRequest().authenticated()
        );
        // 开启基于JWT令牌的资源服务器
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }
}
