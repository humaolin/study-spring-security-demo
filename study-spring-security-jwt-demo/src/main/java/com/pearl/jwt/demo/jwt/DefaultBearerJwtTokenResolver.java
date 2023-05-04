package com.pearl.jwt.demo.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/4
 */
public final class DefaultBearerJwtTokenResolver implements BearerTokenResolver {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE);

    private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;

    public String resolve(final HttpServletRequest request) {
        return resolveFromAuthorizationHeader(request);
    }

    /**
     *  设置此值可配置解析承载令牌时要检查的标头。此值默认为｛@link HttpHeadersAUTHORIZATION｝。
     *
     * This allows other headers to be used as the Bearer Token source such as
     * {@link HttpHeaders#PROXY_AUTHORIZATION}
     * @param bearerTokenHeaderName the header to check when retrieving the Bearer Token.
     * @since 5.4
     */
    public void setBearerTokenHeaderName(String bearerTokenHeaderName) {
        this.bearerTokenHeaderName = bearerTokenHeaderName;
    }

    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader(this.bearerTokenHeaderName);
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            return null;
        }
        Matcher matcher = authorizationPattern.matcher(authorization);
        if (!matcher.matches()) {
            throw new JwtAuthenticationException("Bearer token is malformed");
        }
        return matcher.group("token");
    }
}