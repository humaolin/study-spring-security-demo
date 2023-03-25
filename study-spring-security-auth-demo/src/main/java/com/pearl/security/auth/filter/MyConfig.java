package com.pearl.security.auth.filter;

import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/25
 */
@Configuration
public class MyConfig {

    @Bean
    public DelegatingFilterProxyRegistrationBean delegatingFilterProxyRegistrationBean(){
        DelegatingFilterProxyRegistrationBean filterProxy = new DelegatingFilterProxyRegistrationBean("myFilter");
        filterProxy.addUrlPatterns("/*");
        filterProxy.setOrder(-5);
        return filterProxy;
    }
}
