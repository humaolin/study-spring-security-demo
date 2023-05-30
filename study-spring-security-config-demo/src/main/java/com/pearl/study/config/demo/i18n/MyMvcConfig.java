package com.pearl.study.config.demo.i18n;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.LocaleResolver;

/**
 * Spring国际化默认是通过
 * org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver处理的
 * 从这里可以看出我们只要在application.properties配置spring.mvc.locale=zh_CN 即可实现默认locale
 * StaticMessageSource　　主要用于测试环境，并不用于生产环境
 * SpringSecurityMessageSource　　用于Spring security的国际化信息
 * ReloadableResourceBundleMessageSource　　可以在不用重新启动服务器的情况下，读取更改后的资源文件
 * ResourceBundleMessageSource　　用于生产环境
 * @author TangDan
 * @version 1.0
 * @since 2023/5/30
 */
@Configuration
public class MyMvcConfig  {

    @Bean
    public LocaleResolver localeResolver(){
        return new RequestHeaderLocaleResolver();
    }

/*    @Bean
    public ReloadableResourceBundleMessageSource securityMessageSource(){
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:org/springframework/security/messages");
        return reloadableResourceBundleMessageSource;
    }*/

    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:i18n/messages");
        return reloadableResourceBundleMessageSource;
    }

    @Bean
/*    @ConditionalOnMissingBean({ RequestContextListener.class, RequestContextFilter.class })
    @ConditionalOnMissingFilterBean(RequestContextFilter.class)*/
    public Ii8nRequestContextFilter requestContextFilter(LocaleResolver localeResolver) {
        return new Ii8nRequestContextFilter(false,localeResolver);
    }
}
