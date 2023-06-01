package com.pearl.authorize.demo.dynamic;

import cn.hutool.core.util.ObjectUtil;
import com.pearl.authorize.demo.config.JdbcAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/1
 */
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final Logger log = LoggerFactory.getLogger(JdbcAuthorizationManager.class);

    private static final Map<String, AuthRule> authRules = AuthRule.init();

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        // 1. 可以添加动态添放行路径，然后判断是否放行，若是，则返回 true
        // .... 自己实现
        // 2. 所有请求必须都经过认证
        if (!authentication.get().isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        // 3. 根据请求路径查询权限
        String requestURI = context.getRequest().getRequestURI(); // 请求路径
        AuthRule authRule = authRules.get(requestURI); // 模拟数据库或缓存中查询该路径对应的规则
        if (ObjectUtil.isNotNull(authRule)) {
            // 存在规则，进行校验
            List<GrantedAuthority> roles = authRule.getRoles(); // 该规则需要的所有权限值
            Set<String> authorities = AuthorityUtils.authorityListToSet(roles);
            return new AuthorizationDecision(isAuthorized(authorities, authentication.get()));
        }
        // 原来逻辑
        // 1. 启动加载配置中的所有配置，设置为AuthorizationManager 到过滤器中
        // 2. 每个规则对应一个RequestMatcherEntry
        // 3. 规则判断，返回是否授权
        return new AuthorizationDecision(true);
    }

    /**
     * 循环用户信息中用户的所有权限，判断是否包含当前访问需要的权限
     *
     * @param authorities    设置的规则
     * @param authentication 用户认证对象
     * @return 结果
     */
    private boolean isAuthorized(Set<String> authorities, Authentication authentication) {
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (authorities.contains(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
