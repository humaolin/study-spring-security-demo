package com.pearl.oauth2.demo.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pearl.oauth2.demo.entity.User;
import com.pearl.oauth2.demo.mapper.UserMapper;
import com.pearl.oauth2.demo.service.IUserService;
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
