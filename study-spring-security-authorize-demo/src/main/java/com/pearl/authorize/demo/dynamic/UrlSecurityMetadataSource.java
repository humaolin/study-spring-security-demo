package com.pearl.authorize.demo.dynamic;

import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;
import java.util.Set;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/6/2
 */
public interface UrlSecurityMetadataSource {

    /**
     * 根据请求路径，获取对应权限配置规则
     */
    Set<String> getAttributes(String url) throws IllegalArgumentException;
}
