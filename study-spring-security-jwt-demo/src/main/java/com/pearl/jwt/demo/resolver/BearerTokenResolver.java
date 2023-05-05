package com.pearl.jwt.demo.resolver;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/4
 */
@FunctionalInterface
public interface BearerTokenResolver {

    String resolve(HttpServletRequest request);
}
