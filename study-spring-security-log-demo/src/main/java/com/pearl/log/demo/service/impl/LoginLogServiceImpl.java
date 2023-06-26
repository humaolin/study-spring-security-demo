package com.pearl.log.demo.service.impl;

import com.pearl.log.demo.entity.LoginLog;
import com.pearl.log.demo.mapper.LoginLogMapper;
import com.pearl.log.demo.service.ILoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author pearl
 * @since 2023-06-26
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

}
