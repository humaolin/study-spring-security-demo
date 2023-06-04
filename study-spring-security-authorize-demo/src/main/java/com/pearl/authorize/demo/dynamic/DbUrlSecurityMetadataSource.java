package com.pearl.authorize.demo.dynamic;

import cn.hutool.core.collection.CollUtil;
import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/2
 */
public class DbUrlSecurityMetadataSource implements UrlSecurityMetadataSource {

    @Override
    public Set<String> getAttributes(String url) throws IllegalArgumentException {
        // 1. 模拟数据库查询所有规则
        HashMap<Object, AuthRule> hashMap = new HashMap<>() {{
            put("/test001", new AuthRule("/test001", CollUtil.newHashSet("ROLE_ADMIN")));
            put("/test002", new AuthRule("/test002", CollUtil.newHashSet("ROLE_USER")));
        }};
        // 2. 返回请求URL需要的权限值
        AuthRule authRule = hashMap.get(url);
        return authRule == null ? null : authRule.getAuthoritySet();
    }
}
