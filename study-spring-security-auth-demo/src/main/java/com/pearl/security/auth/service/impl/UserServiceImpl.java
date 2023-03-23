package com.pearl.security.auth.service.impl;

import com.pearl.security.auth.entity.User;
import com.pearl.security.auth.mapper.UserMapper;
import com.pearl.security.auth.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pearl
 * @since 2023-03-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


}
