package com.pearl.study.config.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@SpringBootApplication
public class StudySpringSecurityConfigDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(StudySpringSecurityConfigDemoApplication.class, args);
    }

}
