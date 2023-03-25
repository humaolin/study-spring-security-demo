package com.pearl.security.auth;

import com.pearl.security.auth.filter.MyFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.pearl.security.auth.mapper")
@ServletComponentScan
public class StudySpringSecurityAuthDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(StudySpringSecurityAuthDemoApplication.class, args);
        String[] beanNamesForType = run.getBeanNamesForType(DelegatingFilterProxyRegistrationBean.class);
        System.out.println(beanNamesForType);
    }

}
