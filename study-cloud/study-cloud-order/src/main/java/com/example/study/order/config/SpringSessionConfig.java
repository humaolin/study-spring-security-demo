package com.example.study.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/24
 */
@Configuration
@EnableRedisIndexedHttpSession // 开启支持索引保存会话
public class SpringSessionConfig {
    // @EnableRedisIndexedHttpSession 会自动注册 FindByIndexNameSessionRepository
    // 我们只需要注入进来即可
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    public SpringSessionConfig(FindByIndexNameSessionRepository<? extends Session> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // 使用SpringSessionBackedSessionRegistry，会自动替换默认的 SessionRegistry
/*    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }*/

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
