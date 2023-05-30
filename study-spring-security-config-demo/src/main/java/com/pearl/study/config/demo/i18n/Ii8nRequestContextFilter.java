package com.pearl.study.config.demo.i18n;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/30
 */

public class Ii8nRequestContextFilter extends RequestContextFilter implements OrderedFilter {
    private int order = -105;
    private boolean threadContextInheritable = false;

    private LocaleResolver localeResolver;

    public Ii8nRequestContextFilter(boolean threadContextInheritable, LocaleResolver localeResolver) {
        this.threadContextInheritable = threadContextInheritable;
        this.localeResolver = localeResolver;
    }

    private void initContextHolders(HttpServletRequest request, ServletRequestAttributes requestAttributes) {
        // 使用 RequestHeaderLocaleResolver
        LocaleContextHolder.setLocale(localeResolver.resolveLocale(request), this.threadContextInheritable);
        RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Bound request context to thread: " + request);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        this.initContextHolders(request, attributes);

        try {
            filterChain.doFilter(request, response);
        } finally {
            this.resetContextHolders();
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Cleared thread-bound request context: " + request);
            }

            attributes.requestCompleted();
        }

    }

    private void resetContextHolders() {
        LocaleContextHolder.resetLocaleContext();
        RequestContextHolder.resetRequestAttributes();
    }


    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

}
