package com.pearl.security.auth.security;

import com.pearl.security.auth.handler.JsonAuthenticationFailureHandler;
import com.pearl.security.auth.handler.JsonAuthenticationSuccessHandler;
import com.pearl.security.auth.handler.JsonLogoutSuccessHandler;
import com.pearl.security.auth.handler.MyLogoutHandler;
import com.pearl.security.auth.token.JwtTokenAuthenticationSuccessHandler;
import com.pearl.security.auth.token.JwtTokenSecurityContextHolderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;


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
    @Bean
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
        /* http.formLogin();*/
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
        // 会话管理
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 绝对不会创建Session，也不使用Session，每个请求都需要重新进行身份验证
                );
        // 开启Basic认证
        http.httpBasic();
        //
        http.addFilterAfter(new JwtTokenSecurityContextHolderFilter(), SecurityContextHolderFilter.class);
        // 关闭 CSRF
        http.csrf().disable();
        return http.build();
    }

    /**
     * 密码器
     * 在用户登录认证成功后，后续请求会判断其认证状态并直接放行，那么`Spring Security`是基于什么方式来保存用户
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
