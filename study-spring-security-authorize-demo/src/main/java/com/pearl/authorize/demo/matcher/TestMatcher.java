package com.pearl.authorize.demo.matcher;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.*;

public class TestMatcher {

    public static void main(String[] args) {
        // 请求路径为/demo,并且是 JSON 格式请求
        AndRequestMatcher andRequestMatcher =new AndRequestMatcher(new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON),new AntPathRequestMatcher("/demo"));
        // 请求路径为/test、/demo其中一个
        OrRequestMatcher orRequestMatcher =new OrRequestMatcher(new AntPathRequestMatcher("/test"),new AntPathRequestMatcher("/demo"));
        // URL请求路径全部为英文
        RegexRequestMatcher regexRequestMatcher=new RegexRequestMatcher("^[A-Za-z]+$","POST");
    }
}
