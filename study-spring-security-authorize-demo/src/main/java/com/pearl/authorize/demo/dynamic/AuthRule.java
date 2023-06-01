package com.pearl.authorize.demo.dynamic;

import cn.hutool.core.collection.CollUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/1
 */
public class AuthRule {

    String url;

    List<GrantedAuthority> roles;

    public static Map<String, AuthRule> init() {
        return new HashMap<>() {{
            put("/test001", new AuthRule("/test001", CollUtil.newArrayList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
            put("/test002", new AuthRule("/test002", CollUtil.newArrayList(new SimpleGrantedAuthority("ROLE_USER"))));
        }};
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(List<GrantedAuthority> roles) {
        this.roles = roles;
    }

    public AuthRule(String url, List<GrantedAuthority> roles) {
        this.url = url;
        this.roles = roles;
    }
}
