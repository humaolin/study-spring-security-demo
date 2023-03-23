package com.pearl.security.auth.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pearl.security.auth.entity.User;
import com.pearl.security.auth.security.PearlUserDetails;
import com.pearl.security.auth.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 数据库查询用户
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        if (ObjectUtil.isNull(user)) {
            log.error("Query returned no results for user '" + username + "'");
            throw new UsernameNotFoundException(StrUtil.format("Username {} not found", username));
        } else {
            // 2. 设置权限集合，后续需要数据库查询（授权篇讲解）
            List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("role");
            // 3. 返回UserDetails类型用户
            return new PearlUserDetails(username, user.getPassword(), user.getPhone(), AuthorityUtils.authorityListToSet(authorityList), true, true, true, true);
        }
    }
}
