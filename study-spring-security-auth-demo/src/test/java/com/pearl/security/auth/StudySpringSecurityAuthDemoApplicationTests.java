package com.pearl.security.auth;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pearl.security.auth.entity.User;
import com.pearl.security.auth.security.SM4PasswordEncoder;
import com.pearl.security.auth.service.IUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

@SpringBootTest
class StudySpringSecurityAuthDemoApplicationTests {

    @Autowired
    IUserService userService;

    @Test
    @DisplayName("根据用户名查询用户")
    void testMp() {
        User admin = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, "admin"));
    }

    @Test
    @DisplayName("插入用户数据")
    void insertUserTest() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        User user = new User();
        user.setUserName("zhangsan");
        // 使用加密：{bcrypt} 中的算法，可以从配置文件读取
        user.setPassword( passwordEncoder.encode("{bcrypt}123456"));
        user.setLoginName("zhangsan");
        user.setPhone("13688888888");
        userService.save(user);


        User user2 = new User();
        user2.setUserName("argon2");
        // 使用 argon2 加密
        Argon2PasswordEncoder arg2SpringSecurity = new Argon2PasswordEncoder(16, 32, 1, 65536, 10);
        user2.setPassword(arg2SpringSecurity.encode("{argon2}123456"));
        user2.setLoginName("argon2");
        user2.setPhone("13688888888");
        userService.save(user2);
    }


    @Test
    @DisplayName("SM4测试")
    void SM4Test() {
        String content = "test中文"; // 7c6c77552ae2ed7dbdc1e4bb2bc91726
        SymmetricCrypto sm4 = SmUtil.sm4("1234567812345678".getBytes(StandardCharsets.UTF_8));
        String encryptHex = sm4.encryptHex(content);
        System.out.println(encryptHex);
    }

    @Test
    @DisplayName("插入SM4用户")
    void insertSM4Test() {
        User user = new User();
        user.setUserName("sm4");
        // 使用加密：{sm4} 中的算法，可以从配置文件读取
        user.setPassword( new SM4PasswordEncoder("1234567812345678").encode("123456"));
        user.setLoginName("测试sm4");
        user.setPhone("13688888888");
        userService.save(user);

    }
}
