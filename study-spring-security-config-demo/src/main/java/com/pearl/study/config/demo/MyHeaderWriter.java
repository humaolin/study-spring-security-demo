package com.pearl.study.config.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.header.HeaderWriter;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/29
 */
public class MyHeaderWriter implements HeaderWriter {

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {

    }
}
