package com.pearl.security.auth.security;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.pearl.security.auth.handler.JsonAuthenticationFailureHandler;
import com.pearl.security.auth.handler.JsonLogoutSuccessHandler;
import com.pearl.security.auth.handler.MyLogoutHandler;
import com.pearl.security.auth.token.JwtTokenAuthenticationSuccessHandler;
import com.pearl.security.auth.token.JwtTokenSecurityContextHolderFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableWebSecurity(debug = false)
public class PearlWebSecurityConfig {

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/hello");
    }

    /**
     * HttpSecurity 配置
     */
/*    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests()
                .requestMatchers("/login.html").permitAll()
                .anyRequest().authenticated();
        // 开启表单登录
        http.formLogin()
                // .failureForwardUrl("/login/failure")
                // .failureUrl("/login/failure")
                .successHandler(new JwtTokenAuthenticationSuccessHandler())
                .failureHandler(new JsonAuthenticationFailureHandler())
                //.successForwardUrl("/index") // 自定义登录成功后转发的地址（请求转发，地址栏不变）
                //.defaultSuccessUrl("/index")// 自定义登录成功后重定向的地址（重定向，地址栏变），会优先跳转到登陆前访问的页面，也可以设置总是跳转到该地址
                .loginPage("/login.html") // 自定义登录页面（注意要同步配置loginProcessingUrl）
                .loginProcessingUrl("/custom/login") // 自定义登录处理URL
                .usernameParameter("name") // 自定义用户名参数名称
                .passwordParameter("pwd");   //自定义密码参数名称
        // 开启表单登录
        *//* http.formLogin();*//*
        // 注销登录
*//*        http.logout()
                .logoutSuccessHandler(new JsonLogoutSuccessHandler()) //  自定义注销成功处理器
                //.logoutSuccessUrl("/") // 自定义注销成功跳转地址
                .addLogoutHandler(new MyLogoutHandler()) // 自定义注销处理器
                .clearAuthentication(true) // 清理Authentication ，默认true
                .deleteCookies("xxx", "yyy") // 删除某些指定 cookie
                .invalidateHttpSession(true) // 设置当前登录用户Session（保存登录后的用户信息）无效，默认true
                // 自定义注销请求URL（和 logoutUrl配置只会生效一个）
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/aaa","GET"),
                        new AntPathRequestMatcher("/bbb","GET")));
                //.logoutUrl("/custom/logout"); // 自定义注销登录请求处理路径*//*
        // 会话创建策略
        http.sessionManagement(session -> session
                        .sessionFixation( // 会话固定攻击保护策略
                                SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId
                        )
                        .maximumSessions(1)  // 用户最大会话数为 1，后面的登陆就会自动踢掉前面的登陆
                        .maxSessionsPreventsLogin(true) // 当前已登录时，阻止其他登录
                        .and()
                        .invalidSessionUrl("/login‐view?error=INVALID_SESSION") //  失效跳转路径
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); // 创建策略
        //http.passwordManagement()
        // 开启Basic认证
        http.httpBasic();
        //
        http.addFilterAfter(new JwtTokenSecurityContextHolderFilter(), SecurityContextHolderFilter.class);
        // 关闭 CSRF
        http.csrf().disable();
        return http.build();
    }*/

    /**
     * 密码器
     */

/*    @Bean
    SM4PasswordEncoder passwordEncoder() {
        return new SM4PasswordEncoder("1234567812345678");
    }*/
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 当前需升级到哪种算法 （实际开发需要在配置文件中读取）
        String encodingId = "bcrypt";
        // 添加算法支持
        Map<String, PasswordEncoder> encoders = new HashMap();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("ldap", new LdapShaPasswordEncoder());
        encoders.put("MD4", new Md4PasswordEncoder());
        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_5());
        encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1());
        encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("sha256", new StandardPasswordEncoder());
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_2());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        // 添加自定义密码编码器
        encoders.put("SM4", new SM4PasswordEncoder("1234567812345678"));
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests().anyRequest().authenticated();
        // 开启表单登录
        http.formLogin();
        // 开启Basic认证
        http.httpBasic();
        // 关闭 CSRF
        http.csrf().disable();
        // 开启记住我
        http.rememberMe()
                //.alwaysRemember(true) // 始终开启记住我
                .tokenValiditySeconds(60*60*24*7) // 记住我有效时间
                .rememberMeServices(rememberMeServices()); // 自定义记住我服务类
                //.key("123456") // KEY
        //.useSecureCookie(false) // 是否只支持https
        //.rememberMeCookieDomain("127.0.0.1") // 可以访问该 cookie 的域名
        //.rememberMeCookieName("my-cookie-name") // 配置自定义Cookie 名，默认 remember-me
        //.rememberMeParameter("my-param");  // 传递的参数
        // 会话创建策略
/*        http.sessionManagement(session -> session
                .sessionFixation( // 会话固定攻击保护策略
                        SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId
                )
                .maximumSessions(1)  // 用户最大会话数为 1，后面的登陆就会自动踢掉前面的登陆
                .maxSessionsPreventsLogin(true) // 当前已登录时，阻止其他登录
                .expiredSessionStrategy(event -> {
                    HttpServletResponse response = event.getResponse();
                    response.setContentType("application/json;charset=utf-8"); // 返回JSON
                    response.setStatus(HttpStatus.BAD_REQUEST.value());  // 状态码
                    Map<String, Object> result = new HashMap<>(); // 返回结果
                    result.put("msg", "当前会话已失效");
                    result.put("code", 401);
                    response.getWriter().write(JSONUtil.toJsonStr(result));
                })
                .and()
                .invalidSessionUrl("/login‐view?error=INVALID_SESSION") //  失效跳转路径
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); // 创建策略*/
        // 会话创建策略
        http.sessionManagement(session -> session
                .maximumSessions(1)  // 用户最大会话数为 1，后面的登陆就会自动踢掉前面的登陆
                .maxSessionsPreventsLogin(true)); // 当前已登录时，阻止其他登录
        return http.build();
    }

    @Autowired
    DataSource dataSource;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public RememberMeServices rememberMeServices() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl(); // 数据库存储令牌
        tokenRepository.setDataSource(dataSource);
        // create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)
        tokenRepository.setCreateTableOnStartup(true); //启动时自动创建表结构
        return new PersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(), userDetailsService, tokenRepository);
    }
}