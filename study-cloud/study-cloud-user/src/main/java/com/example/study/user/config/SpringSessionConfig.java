package com.example.study.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/24
 */
@Configuration
public class SpringSessionConfig {

    @Bean
    public HttpSessionIdResolver sessionIdResolver() {
        // X-Auth-Token
        HeaderHttpSessionIdResolver xAuthTokenResolver = HeaderHttpSessionIdResolver.xAuthToken();
        // Authentication-Info
        HeaderHttpSessionIdResolver authenticationInfoResolver = HeaderHttpSessionIdResolver.authenticationInfo();
        // Authorization
        HeaderHttpSessionIdResolver customResolver = new HeaderHttpSessionIdResolver("Authorization");
        return xAuthTokenResolver;
    }

}
