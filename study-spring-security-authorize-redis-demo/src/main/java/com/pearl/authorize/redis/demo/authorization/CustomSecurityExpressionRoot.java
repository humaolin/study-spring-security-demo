package com.pearl.authorize.redis.demo.authorization;//

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import cn.hutool.core.collection.CollUtil;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.util.function.SingletonSupplier;

public abstract class CustomSecurityExpressionRoot implements SecurityExpressionOperations {
    /**
     * 当前登录用户认证信息
     */
    private final Supplier<Authentication> authentication;
    /**
     * 认证对象信任解析程序，解析是否是匿名用户或者记住我用户
     */
    private AuthenticationTrustResolver trustResolver;
    /**
     * RBAC1 角色继承
     */
    private RoleHierarchy roleHierarchy;
    /**
     * 当前用户的角色
     */
    private Set<String> roles;
    /**
     * 角色的前缀（默认：ROLE_）
     */
    private String defaultRolePrefix;
    /**
     * 放行全部
     */
    public final boolean permitAll;
    /**
     * 拒绝所有
     */
    public final boolean denyAll;
    /**
     * 用于计算 hasPermission 表达式
     */
    private PermissionEvaluator permissionEvaluator;
    /**
     * 目标领域对象的增删改查权限标识
     */
    public final String read;
    public final String write;
    public final String create;
    public final String delete;
    public final String admin;


    public CustomSecurityExpressionRoot(Authentication authentication) {
        this(() -> {
            return authentication;
        });
    }

    public CustomSecurityExpressionRoot(Supplier<Authentication> authentication) {
        this.defaultRolePrefix = "ROLE_";
        this.permitAll = true;
        this.denyAll = false;
        this.read = "read";
        this.write = "write";
        this.create = "create";
        this.delete = "delete";
        this.admin = "administration";
        this.authentication = SingletonSupplier.of(() -> {
            Authentication value = authentication.get();
            Assert.notNull(value, "Authentication object cannot be null");
            return value;
        });
    }

    @Override
    public final boolean hasAuthority(String authority) {
        return this.hasAnyAuthority(authority);
    }

    @Override
    public final boolean hasAnyAuthority(String... authorities) {
        return this.hasAnyAuthorityName(null, authorities);
    }

    @Override
    public final boolean hasRole(String role) {
        return this.hasAnyRole(role);
    }

    @Override
    public final boolean hasAnyRole(String... roles) {
        return this.hasAnyAuthorityName(this.defaultRolePrefix, roles);
    }

    private boolean hasAnyAuthorityName(String prefix, String... roles) {
        // 当前用户拥有的角色权限
        Set<String> roleSet = getAuthoritySet();

        // 循环用户的所有角色权限，如果存在访问该方法需要的权限，则返回 true
        for (String role : roles) {
            String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
            if (roleSet.contains(defaultedRole)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public final Authentication getAuthentication() {
        return this.authentication.get();
    }

    @Override
    public final boolean permitAll() {
        return true;
    }

    @Override
    public final boolean denyAll() {
        return false;
    }

    @Override
    public final boolean isAnonymous() {
        return this.trustResolver.isAnonymous(this.getAuthentication());
    }

    @Override
    public final boolean isAuthenticated() {
        return !this.isAnonymous();
    }

    @Override
    public final boolean isRememberMe() {
        return this.trustResolver.isRememberMe(this.getAuthentication());
    }

    @Override
    public final boolean isFullyAuthenticated() {
        Authentication authentication = this.getAuthentication();
        return this.trustResolver.isFullyAuthenticated(authentication);
    }

    public Object getPrincipal() {
        return this.getAuthentication().getPrincipal();
    }

    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        this.trustResolver = trustResolver;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    public void setDefaultRolePrefix(String defaultRolePrefix) {
        this.defaultRolePrefix = defaultRolePrefix;
    }

/*    private Set<String> getAuthoritySet() {
        if (this.roles == null) {
            Collection<? extends GrantedAuthority> userAuthorities = this.getAuthentication().getAuthorities();
            if (this.roleHierarchy != null) {
                userAuthorities = this.roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
            }

            this.roles = AuthorityUtils.authorityListToSet(userAuthorities);
        }

        return this.roles;
    }*/

    /**
     * 从数据库或缓存中查询权限
     */
    private Set<String> getAuthoritySet() {
        if (this.roles == null) {
            // 1. 模拟查询用户角色、权限
            // 根据用户名（实际开发可以使用用户ID）缓存中查询权限
            String username = this.getAuthentication().getName();
            Set<String> userAuthorities = UserAuthoritiesCache.authoritySet.get(username);
            // 缓存中没有，可以查询数据库再放入缓存
            if (CollUtil.isEmpty(userAuthorities)) {
                userAuthorities = new HashSet<>();
                userAuthorities.add("ROLE_USER");
                userAuthorities.add("user:list");
                UserAuthoritiesCache.authoritySet.put(username,userAuthorities);
            }

            // 2. 角色继承处理
            if (this.roleHierarchy != null) {
                // userAuthorities = this.roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
            }
            this.roles = userAuthorities;
        }
        // 3. 返回角色
        return this.roles;
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return this.permissionEvaluator.hasPermission(this.getAuthentication(), target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return this.permissionEvaluator.hasPermission(this.getAuthentication(), (Serializable) targetId, targetType, permission);
    }

    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }

    private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
        if (role == null) {
            return role;
        } else if (defaultRolePrefix != null && defaultRolePrefix.length() != 0) {
            return role.startsWith(defaultRolePrefix) ? role : defaultRolePrefix + role;
        } else {
            return role;
        }
    }
}
