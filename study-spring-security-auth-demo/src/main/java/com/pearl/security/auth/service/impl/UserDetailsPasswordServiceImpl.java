package com.pearl.security.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pearl.security.auth.entity.User;
import com.pearl.security.auth.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/4/3
 */
@Service
@RequiredArgsConstructor
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

    private final IUserService userService;

    /**
     *
     * 登录时自动升级密码
     * @param user 用户
     * @param newPassword 新密码
     * @return 用户信息
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        // 升级密码
        LambdaUpdateWrapper<User> queryWrapper = new LambdaUpdateWrapper<User>()
                .eq(User::getUserName, user.getUsername())
                .set(User::getPassword,newPassword);
        userService.update(queryWrapper);
        return user;
    }
}
