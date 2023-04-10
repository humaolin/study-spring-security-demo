package com.pearl.authorize.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

/**
 * Spring Security提供的AuthorityAuthorizationManager。
 * 它被配置为在当前 Authentication 中寻找一组给定的授权。
 * 如果 Authentication 包含任何配置的授权，
 * 它将返回 positive 的 AuthorizationDecision。否则，它将返回一个 negative 的 AuthorizationDecision
 *
 * AuthenticatedAuthorizationManager
 * 另一个管理器是 AuthenticatedAuthorizationManager。它可以用来区分匿名、完全认证和记住我认证的用户。许多网站在Remember-me认证下允许某些有限的访问，但要求用户通过登录来确认他们的身份以获得完整的访问。
 * RoleHierarchy 角色分层
 *
 *
 *
 * 你可以创建一个可以查询Open Policy Agent或你自己的授权数据库的实现。
 * @author TangDan
 * @version 1.0
 * @since 2023/4/10
 */
public class JdbcAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest object) {
        return null;
    }
}
