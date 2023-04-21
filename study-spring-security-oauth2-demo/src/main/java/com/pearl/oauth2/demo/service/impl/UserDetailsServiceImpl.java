package com.pearl.oauth2.demo.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pearl.oauth2.demo.entity.User;
import com.pearl.oauth2.demo.security.PearlUserDetails;
import com.pearl.oauth2.demo.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
            List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
            // 3. 返回UserDetails类型用户
            return new PearlUserDetails(username, user.getPassword(), user.getPhone(), authorityList,
                    true, true, true, true); // 账号状态这里都直接设置为启用，实际业务可以存在数据库中
        }
    }

    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        // 1. 数据库查询用户
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (ObjectUtil.isNull(user)) {
            log.error("Query returned no results for user '" + phone + "'");
            throw new UsernameNotFoundException(StrUtil.format("Phone {} not found", phone));
        } else {
            // 2. 设置权限集合，后续需要数据库查询（授权篇讲解）
            List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
            // 3. 返回UserDetails类型用户
            return new PearlUserDetails(user.getUserName(), null, user.getPhone(), authorityList,
                    true, true, true, true); // 账号状态这里都直接设置为启用，实际业务可以存在数据库中
        }
    }
}
