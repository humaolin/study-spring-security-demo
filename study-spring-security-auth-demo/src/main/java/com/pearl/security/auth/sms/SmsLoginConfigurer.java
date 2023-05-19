package com.pearl.security.auth.sms;

import com.pearl.security.auth.handler.JsonAuthenticationFailureHandler;
import com.pearl.security.auth.handler.JsonAuthenticationSuccessHandler;
import com.pearl.security.auth.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public final class SmsLoginConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractAuthenticationFilterConfigurer<H, SmsLoginConfigurer<H>, SmsAuthenticationFilter> {
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    private SavedRequestAwareAuthenticationSuccessHandler defaultSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    private AuthenticationSuccessHandler successHandler;
    private LoginUrlAuthenticationEntryPoint authenticationEntryPoint;

    private AuthenticationFailureHandler failureHandler;


    public SmsLoginConfigurer() {
        super(new SmsAuthenticationFilter(), "/sms/login");
        this.failureHandler = new JsonAuthenticationFailureHandler();
        this.successHandler = new JsonAuthenticationSuccessHandler();
    }

    @Override
    public void configure(H http) throws Exception {
        PortMapper portMapper = http.getSharedObject(PortMapper.class);
        if (portMapper != null) {
            this.authenticationEntryPoint.setPortMapper(portMapper);
        }
        RequestCache requestCache =http.getSharedObject(RequestCache.class);
        if (requestCache != null) {
            this.defaultSuccessHandler.setRequestCache(requestCache);
        }
        // 添加认证提供者
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        UserDetailsServiceImpl userDetailService = http.getSharedObject(ApplicationContext.class).getBean(UserDetailsServiceImpl.class);
        Assert.notNull(userDetailService,"UserDetailsService 不能为空");
        StringRedisTemplate stringRedisTemplate = http.getSharedObject(ApplicationContext.class).getBean(StringRedisTemplate.class);
        Assert.notNull(stringRedisTemplate,"StringRedisTemplate 不能为空");
        http.authenticationProvider(new SmsAuthenticationProvider(userDetailService, stringRedisTemplate));
        // 设置过滤器
        SmsAuthenticationFilter smsAuthenticationFilter = this.getAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(authenticationManager);
        smsAuthenticationFilter.setAuthenticationSuccessHandler(this.successHandler);
        smsAuthenticationFilter.setAuthenticationFailureHandler(this.failureHandler);
        if (this.authenticationDetailsSource != null) {
            smsAuthenticationFilter.setAuthenticationDetailsSource(this.authenticationDetailsSource);
        }
        SessionAuthenticationStrategy sessionAuthenticationStrategy =http.getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            smsAuthenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }
        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            smsAuthenticationFilter.setRememberMeServices(rememberMeServices);
        }
        SecurityContextConfigurer<H> securityContextConfigurer = http.getConfigurer(SecurityContextConfigurer.class);
        if (securityContextConfigurer != null) {
            SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
            if (securityContextRepository == null) {
                securityContextRepository = new DelegatingSecurityContextRepository(
                        new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
            }
            smsAuthenticationFilter.setSecurityContextRepository(securityContextRepository);
        }
        smsAuthenticationFilter.setSecurityContextHolderStrategy(this.getSecurityContextHolderStrategy());
        SmsAuthenticationFilter filter = this.postProcess(smsAuthenticationFilter);
        http.addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }


    public SmsLoginConfigurer<H> loginPage(String loginPage) {
        return super.loginPage(loginPage);
    }

    public SmsLoginConfigurer<H> phoneParameter(String phoneParameter) {
        (this.getAuthenticationFilter()).setPhoneParameter(phoneParameter);
        return this;
    }

    public SmsLoginConfigurer<H> smsCodeParameter(String smsCodeParameter) {
        (this.getAuthenticationFilter()).setSmsCodeParameter(smsCodeParameter);
        return this;
    }

    public SmsLoginConfigurer<H> failureForwardUrl(String forwardUrl) {
        this.failureHandler(new ForwardAuthenticationFailureHandler(forwardUrl));
        return this;
    }

    public SmsLoginConfigurer<H> successForwardUrl(String forwardUrl) {
        this.successHandler(new ForwardAuthenticationSuccessHandler(forwardUrl));
        return this;
    }

    public void init(H http) throws Exception {
        super.init(http);
        this.initDefaultLoginFilter(http);
    }

    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    private String getPhoneParameter() {
        return (this.getAuthenticationFilter()).getPhoneParameter();
    }

    private String getSmsCodeParameter() {
        return (this.getAuthenticationFilter()).getSmsCodeParameter();
    }

    private void initDefaultLoginFilter(H http) {
        DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = http.getSharedObject(DefaultLoginPageGeneratingFilter.class);
        if (loginPageGeneratingFilter != null && !this.isCustomLoginPage()) {
            loginPageGeneratingFilter.setFormLoginEnabled(true);
            loginPageGeneratingFilter.setUsernameParameter(this.getPhoneParameter());
            loginPageGeneratingFilter.setPasswordParameter(this.getSmsCodeParameter());
            loginPageGeneratingFilter.setLoginPageUrl(this.getLoginPage());
            loginPageGeneratingFilter.setFailureUrl(this.getFailureUrl());
            loginPageGeneratingFilter.setAuthenticationUrl(this.getLoginProcessingUrl());
        }
    }

    public static SmsLoginConfigurer<HttpSecurity> smsLogin() {
        return new SmsLoginConfigurer<>();
    }
}
