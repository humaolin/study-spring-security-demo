package com.pearl.security.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pearl.security.auth.mapper")
public class StudySpringSecurityAuthDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringSecurityAuthDemoApplication.class, args);
    }

}
