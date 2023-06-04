package com.pearl.study.config.demo.i18n;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;


public class PearlSecurityMessageSource extends ResourceBundleMessageSource {

    private PearlSecurityMessageSource() {
        this.setBasename("classpath:/i18n/messages");
    }

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new PearlSecurityMessageSource());
    }
}