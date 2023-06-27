package com.pearl.log.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pearl.log.demo.mapper")
public class StudySpringSecurityLogDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringSecurityLogDemoApplication.class, args);
    }

}
