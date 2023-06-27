package com.pearl.log.demo.service.impl;

import com.pearl.log.demo.entity.User;
import com.pearl.log.demo.mapper.UserMapper;
import com.pearl.log.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pearl
 * @since 2023-06-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
