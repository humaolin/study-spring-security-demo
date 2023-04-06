package com.pearl.security.auth.filter;

import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/25
 */
@Configuration
public class MyConfig {

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        // 使用SpringSession时，不再需要
        return new HttpSessionEventPublisher();
    }

    @Bean
    public DelegatingFilterProxyRegistrationBean delegatingFilterProxyRegistrationBean(){
        DelegatingFilterProxyRegistrationBean filterProxy = new DelegatingFilterProxyRegistrationBean("myFilter");
        filterProxy.addUrlPatterns("/*");
        filterProxy.setOrder(-5);
        return filterProxy;
    }
}
