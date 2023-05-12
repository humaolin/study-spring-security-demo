package com.pearl.jwt.demo.jwt;

import cn.hutool.json.JSONException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import com.pearl.jwt.demo.resolver.BearerTokenResolver;
import com.pearl.jwt.demo.resolver.DefaultBearerJwtTokenResolver;
import com.pearl.jwt.demo.response.R;
import com.pearl.jwt.demo.response.ResponseUtils;
import com.pearl.jwt.demo.response.ResultCodeEnum;
import com.pearl.jwt.demo.validator.HutoolJwtValidator;
import com.pearl.jwt.demo.validator.JwtValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


/**
 *
 *
 * @author TangDan
 * @version 1.0
 * @since 2023/3/30
 */
@Getter
@Setter
public class JwtTokenSecurityContextHolderFilter extends GenericFilterBean {

    private static final String FILTER_APPLIED = JwtTokenSecurityContextHolderFilter.class.getName() + ".APPLIED";
    private BearerTokenResolver bearerTokenResolver;
    private JwtValidator jwtValidator = new HutoolJwtValidator();
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private UserDetailsService userDetailsService;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request.getAttribute(FILTER_APPLIED) != null) {
            chain.doFilter(request, response);
        } else {
            String token;
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            // 1. 解析请求中的令牌
            try {
                token = this.bearerTokenResolver.resolve(request);
            } catch (JwtAuthenticationException e) {
                ResponseUtils.buildResponse(response, R.response(ResultCodeEnum.JWT_TOKEN_FORMAT_ERROR, null), HttpStatus.UNAUTHORIZED);
                return;
            }
            // 2. 令牌不存在，直接进入下一个过滤器
            if (token == null) {
                chain.doFilter(request, response);
            } else {
                // 3. 存在令牌
                try {
                    // 3.1 校验
                    String username = jwtValidator.validate(token);
                    // 3.2 根据标识组装用户信息，实际开发可以使用缓存
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    // 3.3 组装认证信息
                    JwtAuthenticationToken authentication = JwtAuthenticationToken.authenticated(userDetails, token, userDetails.getAuthorities());
                    // 3.3 创建安装上下文
                    SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                    context.setAuthentication(authentication);
                    // 3.4 保存到Holder、存储中
                    this.securityContextHolderStrategy.setContext(context);
                    this.securityContextRepository.saveContext(context, request, response);
                    chain.doFilter(request, response);
                } catch (JWTException | JSONException | JwtAuthenticationException e) {
                    //e.printStackTrace();
                    ResponseUtils.buildResponse(response,
                            R.response(HttpStatus.UNAUTHORIZED.value(), e.getLocalizedMessage(), null),
                            HttpStatus.UNAUTHORIZED);
                } finally {
                    request.removeAttribute(FILTER_APPLIED);
                }
            }
        }
    }
}
