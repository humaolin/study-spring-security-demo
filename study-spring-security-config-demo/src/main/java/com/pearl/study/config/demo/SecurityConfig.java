package com.pearl.study.config.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.debug.DebugFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import static com.pearl.study.config.demo.MyCustomDsl.customDsl;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 *
 */
@Configuration
public class SecurityConfig {

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .debug(false) // 开启DEBUG
                //.httpFirewall() // 防火请
                //.expressionHandler() // 注解权限表达式处理器
                .ignoring().requestMatchers("/resources/**", "/static/**"); // 配置放行路径，不会经过过滤器一般用于静态资源
    }

    /**
     * 前后端分离
     */
/*    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        // http.authorizeHttpRequests().anyRequest().authenticated(); // 过时写法
        http.authorizeHttpRequests( // lambda 写法
                authorizeHttpRequestsCustomizer -> {
                    authorizeHttpRequestsCustomizer.requestMatchers("/test").permitAll();
                    authorizeHttpRequestsCustomizer.anyRequest().authenticated();
                }
        );

        http.formLogin(
                formLoginCustomizer -> {
                    formLoginCustomizer.usernameParameter("name")
                            .passwordParameter("pwd");
                }
        );
        // 关闭CSRF
        http.csrf(
                csrfCustomizer -> {
                    csrfCustomizer
                            // 配置忽略的路径
                            .ignoringRequestMatchers("/hello");
                }
        );
        // Session会话管理
        http.sessionManagement(
                sessionManagementCustomizer -> {
                    // 最大会话数
                    sessionManagementCustomizer.maximumSessions(1);
                }
        );
        // 过滤器链匹配路径
        http.securityMatcher("/**");
        // 自定义配置
        //http.apply()
        // 添加过滤器
        http.addFilter(new MyFilter());
        http.addFilterBefore(new MyFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new MyFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(new MyFilter(), UsernamePasswordAuthenticationFilter.class);
        // 异常处理器
        http.exceptionHandling(
                exceptionHandlingCustomizer->{
                    exceptionHandlingCustomizer
                            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                            .accessDeniedHandler(new AccessDeniedHandlerImpl())
                            .accessDeniedPage("/aaa");
                }
        );
        // 添加认证提供者
        http.authenticationProvider(new AnonymousAuthenticationProvider("key"));
        // 记住我
        http.rememberMe(
                rememberMeCustomizer->{
                    rememberMeCustomizer.key("");
                }
        );
        // Basic认证
        http.httpBasic(
                httpBasicCustomizer->{
                    httpBasicCustomizer.realmName("name");
                }
        );
        // 匿名用户
        http.anonymous(
                anonymousCustomizer->{
                    anonymousCustomizer.key("key");
                }
        );
        // 登出
        http.logout(
                logoutCustomizer->{
                    logoutCustomizer.logoutUrl("/logout");
                }
        );
        // Oauth2 登录配置
        http.oauth2Login(
                oauth2LoginCustomizer->{
                    //oauth2LoginCustomizer.redirectionEndpoint()
                }
        );
        // Oauth2 客户端配置
        http.oauth2Client(
                oauth2ClientCustomizer->{
                    // oauth2ClientCustomizer.authorizedClientRepository( );
                }
        );
        // Oauth2 资源服务器配置
        http.oauth2ResourceServer(
                oauth2ResourceServerCustomizer->{
                    oauth2ResourceServerCustomizer.accessDeniedHandler(new AccessDeniedHandlerImpl());
                }
        );
        // CORS 跨域配置
        http.cors(
                corsCustomizer->{
                    corsCustomizer.configurationSource();
                }
        );
        // SAML2 登录
        http.saml2Login(
                saml2LoginCustomizer->{
                    saml2LoginCustomizer.loginPage("/login");
                }
        );
        // 修改密码
        http.passwordManagement(
                passwordManagementCustomizer->{
                    passwordManagementCustomizer.changePasswordPage("/changePwd");
                }
        );
        return http.build();
    }*/
    @Bean
    @Order(0) // 定义顺序，数值越小越靠前
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**") // 过滤器链匹配路径
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasRole("ADMIN")
                );
        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .headers(
                        headersCustomizer -> {
                            // 后置处理器，设置HeaderWriterFilter
                            headersCustomizer.addObjectPostProcessor(new ObjectPostProcessor<HeaderWriterFilter>() {
                                @Override
                                public <O extends HeaderWriterFilter> O postProcess(O object) {
                                    object.setShouldWriteHeadersEagerly(false);
                                    return object;
                                }
                            });
                            // 添加自定义响应头
                            headersCustomizer.addHeaderWriter(new StaticHeadersWriter("X-Custom-Security-Header", "header-value"));
                            // 关闭  Cache Control
                            headersCustomizer.cacheControl(
                                    HeadersConfigurer.CacheControlConfig::disable
                            );
                            // 禁用所有默认的响应头
                            headersCustomizer.defaultsDisabled();
                            // HSTS配置
                            headersCustomizer.httpStrictTransportSecurity(
                                    hstsCustomizer -> {
                                        hstsCustomizer.includeSubDomains(true)
                                                .maxAgeInSeconds(52552552L)
                                                .preload(true);
                                    }
                            );
                            // 配置 XXssProtectionHeaderWriter
                            headersCustomizer.xssProtection(
                                    xss -> xss
                                            .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                            );
                        }
                )
                // 开启表单登录
                .formLogin(
                        formLoginCustomizer -> {
                            formLoginCustomizer.withObjectPostProcessor(new ObjectPostProcessor<UsernamePasswordAuthenticationFilter>() {
                                @Override
                                public <O extends UsernamePasswordAuthenticationFilter> O postProcess(O object) {
                                    object.setPostOnly(false);
                                    object.setAllowSessionCreation(true);
                                    return object;
                                }
                            });
                            //formLoginCustomizer.usernameParameter("user");
                        }
                )
                // 开启Basic认证
                .httpBasic(withDefaults());
        // 自定义配置
        http.apply(customDsl())
                .flag(true);
        // 禁用自定义配置
        // http.apply(customDsl()).disable();
        return http.build();
    }

    @Autowired
    private MessageSource messageSource;


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