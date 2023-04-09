package com.pearl.security.auth.sms;

import com.pearl.security.auth.handler.JsonAuthenticationFailureHandler;
import com.pearl.security.auth.handler.JsonAuthenticationSuccessHandler;
import com.pearl.security.auth.service.impl.UserDetailsServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/7
 */
public class SmsLoginConfigurer extends AbstractHttpConfigurer<SmsLoginConfigurer, HttpSecurity> {


    @Override
    public void init(HttpSecurity http) throws Exception {
        // 初始化方法
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置方法
        // 添加认证提供者
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        UserDetailsServiceImpl userDetailService = http.getSharedObject(ApplicationContext.class).getBean(UserDetailsServiceImpl.class);
        StringRedisTemplate stringRedisTemplate = http.getSharedObject(ApplicationContext.class).getBean(StringRedisTemplate.class);
        http.authenticationProvider(new SmsAuthenticationProvider(userDetailService, stringRedisTemplate));
        // 添加过滤器
        SmsAuthenticationFilter filter = new SmsAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new JsonAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new JsonAuthenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManager);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    public static SmsLoginConfigurer smsLogin() {
        return new SmsLoginConfigurer();
    }
}
