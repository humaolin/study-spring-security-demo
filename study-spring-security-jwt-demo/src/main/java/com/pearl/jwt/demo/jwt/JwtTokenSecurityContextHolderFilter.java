package com.pearl.jwt.demo.jwt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import com.pearl.jwt.demo.response.R;
import com.pearl.jwt.demo.response.ResponseUtils;
import com.pearl.jwt.demo.response.ResultCodeEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/30
 */
public class JwtTokenSecurityContextHolderFilter extends GenericFilterBean {
    private static final String FILTER_APPLIED = JwtTokenSecurityContextHolderFilter.class.getName() + ".APPLIED";

    private BearerTokenResolver bearerTokenResolver = new DefaultBearerJwtTokenResolver();

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token;
        try {
            // 解析请求中的令牌
            token = this.bearerTokenResolver.resolve(request);
        } catch (JwtAuthenticationException exception) {
            // 令牌格式校验异常
            // AuthenticationEntryPoint 直接结束请求，并响应错误信息
            this.authenticationEntryPoint.commence(request, response, exception);
            return;
        }
        // 令牌不存在，直接进入下一个过滤器
        if (token == null) {
            chain.doFilter(request, response);
        } else {
            try {
                // 已执行过，直接放行
                if (request.getAttribute(FILTER_APPLIED) != null) {
                    chain.doFilter(request, response);
                } else {
                    // 设置已执行标记
                    request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
                    // 请求头中获取 token
                    // JWT验证
                    boolean verify = JWTUtil.verify(token, AuthenticationConstants.JWT_KEY.getBytes());
                    if (!verify) {
                        ResponseUtils.buildResponse(response, R.response(ResultCodeEnum.JWT_TOKEN_VERIFY_FAIL, null), HttpStatus.UNAUTHORIZED);
                        return;
                    }
                    // 已过期
                    final JWT jwt = JWTUtil.parseToken(token);
                    jwt.getHeader(JWTHeader.TYPE);
                    long expireTime = (long) jwt.getPayload("expire_time");
                    if (System.currentTimeMillis() >= expireTime) {
                        ResponseUtils.buildResponse(response, R.response(ResultCodeEnum.JWT_TOKEN_EXPIRED, null), HttpStatus.UNAUTHORIZED);
                        return;
                    }
                    // 取出认证信息并放入SecurityContextHolder
                    Object obj = jwt.getPayload("authentication");
                    JSONObject jsonObject = JSONUtil.parseObj(obj);
                    JSONArray jsonArray = jsonObject.getJSONArray("authorities"); //权限
                    List<String> list = jsonArray.toList(String.class);
                    String[] strings = list.toArray(new String[list.size()]);
                    List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
                    JSONObject principal = JSONUtil.parseObj(jsonObject.get("principal")); // 用户信息
                    Object details = jsonObject.get("details");// details (远程地址、SessionID)
                    User user = new User(null, null, authorityList);
                    JwtAuthenticationToken authentication = JwtAuthenticationToken.authenticated(user, null, authorityList);
                    authentication.setDetails(details);
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);
                    chain.doFilter(request, response);
                    // 认证通过后，SecurityContext 保存到Holder、存储中
                    SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                    context.setAuthentication(authentication);
                    this.securityContextHolderStrategy.setContext(context);
                    this.securityContextRepository.saveContext(context, request, response);
                    chain.doFilter(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                SecurityContextHolder.clearContext();
                request.removeAttribute(FILTER_APPLIED);
            }
            // 使用令牌创建预认证对象
/*            BearerTokenAuthenticationToken authenticationRequest = new BearerTokenAuthenticationToken(token);            authenticationRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
            try {
                // JwtAuthenticationProvider
                AuthenticationManager authenticationManager = this.authenticationManagerResolver.resolve(request);
                // 解析令牌，进行验证
                Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
                // 认证通过后，SecurityContext 保存到Holder、存储中
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authenticationResult);
                this.securityContextHolderStrategy.setContext(context);
                this.securityContextRepository.saveContext(context, request, response);
                filterChain.doFilter(request, response);
            } catch (AuthenticationException var9) {
                // 认证失败处理
                this.securityContextHolderStrategy.clearContext();
                this.authenticationFailureHandler.onAuthenticationFailure(request, response, var9);
            }*/
        }

    }
}
