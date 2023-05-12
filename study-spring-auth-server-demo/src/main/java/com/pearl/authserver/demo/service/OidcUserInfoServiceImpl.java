package com.pearl.authserver.demo.service;

import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/5/10
 */
@Service
public class OidcUserInfoServiceImpl implements OidcUserInfoService {
    @Override
    public OidcUserInfo loadUser(String username, Set<String> scopes) {
        OidcUserInfo.Builder builder = OidcUserInfo.builder()
                .email("163@qq.com")// 邮箱
                .subject(username); // 主体
        // 构建用户信息（实际开发需要从数据库查询）
        // profile 范围对应的用户信息
        if (scopes.contains(OidcScopes.PROFILE)) {
            builder.nickname("昵称")
                    .preferredUsername(username) // 用户名
                    .picture("http://127.0.0.1:8080/ssiuoidus.jpg") // 头像地址
                    .gender("女") // 性别
                    .birthdate("2023-05-10"); // 生日
        }
        // 手机号
        if (scopes.contains(OidcScopes.PHONE)) {
            builder.phoneNumber("13688888888").phoneNumberVerified(true);
        }
        // 邮箱
        if (scopes.contains(OidcScopes.EMAIL)) {
            builder.email("13688888888@qq.com");
        }
        //.... 其他
        return builder.build();
    }
}
