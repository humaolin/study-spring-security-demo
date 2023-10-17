/*
package com.pearl.authorize.redis.demo;

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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * @author TangDan
 * @version 1.0
 * @since 2023/3/23
 *//*

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 数据库查询用户
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        if (ObjectUtil.isNull(user)) {
            throw new UsernameNotFoundException(StrUtil.format("Username {} not found", username));
        } else {
            // 2. 权限集合（实际需要在数据库中查询）
            List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
            // 可以在登录时将权限信息设置到缓存中...
            // 省略.........
            // 3. 返回UserDetails类型用户（返回空的权限，后续校验时直接在缓存中查询 ）
            return new User(username, user.getPassword(),
                    true, true, true, true, new ArrayList<>());
        }

        public UserDetails loadUserByPhone (String phone) throws UsernameNotFoundException {
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
*/
