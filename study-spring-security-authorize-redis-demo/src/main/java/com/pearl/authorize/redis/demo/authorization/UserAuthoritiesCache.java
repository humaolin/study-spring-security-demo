package com.pearl.authorize.redis.demo.authorization;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TD
 * @version 1.0
 * @date 2023/10/16
 */
public class UserAuthoritiesCache {

    // 模拟缓存中存储用户权限，实际开发需要使用缓存数据库
   public static ConcurrentHashMap<String, Set<String>> authoritySet = new ConcurrentHashMap<>();
}
