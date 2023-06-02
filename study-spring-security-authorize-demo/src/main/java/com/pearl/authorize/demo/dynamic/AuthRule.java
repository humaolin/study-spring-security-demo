package com.pearl.authorize.demo.dynamic;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 新建
 * @author TangDan
 * @version 1.0
 * @since 2023/6/1
 */
@Data
@AllArgsConstructor
public class AuthRule {

    // 请求路径
    String url;

    // 请求路径对应的 角色+权限值
    Set<String> authoritySet;
}
