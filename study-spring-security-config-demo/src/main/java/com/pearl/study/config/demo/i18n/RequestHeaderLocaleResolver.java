package com.pearl.study.config.demo.i18n;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/30
 */
public class RequestHeaderLocaleResolver implements LocaleResolver {

    // 请求头参数
    private static final String LANG_HEADER = "lang";

    // 解析请求，设置Locale地区化
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 1. 获取请求中的lang参数
        String lang = request.getHeader(LANG_HEADER);
        // 2. 没有lang参数，使用默认Locale
        if (StringUtils.isEmpty(lang)) {
            return Locale.getDefault();
        } else {
            // 4. 根据lang参数，设置国际化，分割出国家和地址
            // lang: en_US
            String[] s = lang.split("_");
            return new Locale(s[0], s[1]);
        }
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
