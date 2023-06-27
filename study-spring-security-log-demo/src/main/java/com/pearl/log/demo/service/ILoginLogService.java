package com.pearl.log.demo.service;

import com.pearl.log.demo.entity.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * <p>
 * 系统访问记录 服务类
 * </p>
 *
 * @author pearl
 * @since 2023-06-26
 */
public interface ILoginLogService extends IService<LoginLog> {

    void save(LoginLog log, HttpServletRequest request);

}
