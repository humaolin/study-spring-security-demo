package com.pearl.study.config.demo.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/30
 */
@Component
public class MessageSourceUtils {

    public MessageSourceUtils() {
    }

    private static MessageSource messageSource;

    @Autowired
    public MessageSourceUtils(MessageSource messageSource) {
        MessageSourceUtils.messageSource = messageSource;
    }

    public static String getMsg(String code) {
        // TODO 使用MessageSourceAccessor、SpringSecurityMessageSource
        // LocaleContextHolder 中封装了Locale 对象；
        Locale locale = LocaleContextHolder.getLocale();
        // MessageSource中获取国际化信息
        return messageSource.getMessage(code, null, locale);
    }
}

