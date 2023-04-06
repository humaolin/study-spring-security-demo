package com.pearl.security.auth.security;

import cn.hutool.json.JSONUtil;
import com.pearl.security.auth.handler.JsonAuthenticationFailureHandler;
import com.pearl.security.auth.token.JwtTokenAuthenticationSuccessHandler;
import com.pearl.security.auth.token.JwtTokenSecurityContextHolderFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
/*import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;*/

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity(debug = true)
public class PearlWebSecurityConfig {

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/hello");
    }

    /**
     * HttpSecurity 配置
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 配置所有的Http请求必须认证
        http.authorizeHttpRequests()
                .requestMatchers("/login.html").permitAll()
                .anyRequest().authenticated();
        // 开启表单登录
/*        http.formLogin()
                // .failureForwardUrl("/login/failure")
                // .failureUrl("/login/failure")
                .successHandler(new JwtTokenAuthenticationSuccessHandler())
                .failureHandler(new JsonAuthenticationFailureHandler())
                //.successForwardUrl("/index") // 自定义登录成功后转发的地址（请求转发，地址栏不变）
                //.defaultSuccessUrl("/index")// 自定义登录成功后重定向的地址（重定向，地址栏变），会优先跳转到登陆前访问的页面，也可以设置总是跳转到该地址
                .loginPage("/login.html") // 自定义登录页面（注意要同步配置loginProcessingUrl）
                .loginProcessingUrl("/custom/login") // 自定义登录处理URL
                .usernameParameter("name") // 自定义用户名参数名称
                .passwordParameter("pwd");   //自定义密码参数名称*/
        // 开启表单登录
        http.formLogin();
        // 注销登录
/*        http.logout()
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
                //.logoutUrl("/custom/logout"); // 自定义注销登录请求处理路径*/
        // 会话创建策略
        http.sessionManagement(session -> session
                .sessionFixation( // 会话固定攻击保护策略
                        SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId
                )
                .maximumSessions(1)  // 用户最大会话数为 1，后面的登陆就会自动踢掉前面的登陆
                .maxSessionsPreventsLogin(true) // 当前已登录时，阻止其他登录
                .sessionRegistry(springSessionBackedSessionRegistry)  // 指定会话注册表
                .and()
                //.invalidSessionUrl("/login‐view?error=INVALID_SESSION") //  失效跳转路径
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        // 开启Basic认证
        http.httpBasic();
        //http.addFilterAfter(new JwtTokenSecurityContextHolderFilter(), SecurityContextHolderFilter.class);
        // 关闭 CSRF
        http.csrf().disable();
        return http.build();
    }

    SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry;


    /**
     * 密码器
     * 在用户登录认证成功后，后续请求会判断其认证状态并直接放行，那么`Spring Security`是基于什么方式来保存用户
     */
/*    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        // 使用SpringSession时，不再需要
        return new HttpSessionEventPublisher();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
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
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }
}
