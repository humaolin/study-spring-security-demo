package com.pearl.authorize.demo.spel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/11
 */
@Configuration
public class MethodSecurityConfig  {

    @Bean
    protected MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler();
    }
}

