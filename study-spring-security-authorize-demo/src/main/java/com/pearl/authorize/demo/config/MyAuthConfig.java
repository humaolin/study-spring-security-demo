package com.pearl.authorize.demo.config;

import com.pearl.authorize.demo.dynamic.CustomAuthorizationManager;
import com.pearl.authorize.demo.dynamic.DbUrlSecurityMetadataSource;
import com.pearl.authorize.demo.dynamic.UrlSecurityMetadataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/2
 */
@Configuration(proxyBeanMethods = false)
public class MyAuthConfig {

    @Bean
    public CustomAuthorizationManager customAuthorizationManager(UrlSecurityMetadataSource urlSecurityMetadataSource) {
        return new CustomAuthorizationManager(urlSecurityMetadataSource);
    }

    @Bean
    public UrlSecurityMetadataSource urlSecurityMetadataSource() {
        return new DbUrlSecurityMetadataSource();
    }
}
