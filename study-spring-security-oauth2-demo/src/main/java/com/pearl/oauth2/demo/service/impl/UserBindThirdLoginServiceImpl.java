package com.pearl.oauth2.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pearl.oauth2.demo.entity.UserBindThirdLogin;
import com.pearl.oauth2.demo.mapper.UserBindThirdLoginMapper;
import com.pearl.oauth2.demo.service.IUserBindThirdLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pearl
 * @since 2023-04-23
 */
@Service
public class UserBindThirdLoginServiceImpl extends ServiceImpl<UserBindThirdLoginMapper, UserBindThirdLogin> implements IUserBindThirdLoginService {

    @Override
    public List<UserBindThirdLogin> selectListByUserId(String userId) {
        LambdaQueryWrapper<UserBindThirdLogin> query = Wrappers.lambdaQuery();
        query.eq(UserBindThirdLogin::getUserId,userId);
        return list(query);
    }
}
