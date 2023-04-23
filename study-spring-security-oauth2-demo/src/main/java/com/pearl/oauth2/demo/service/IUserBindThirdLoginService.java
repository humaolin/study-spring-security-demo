package com.pearl.oauth2.demo.service;

import com.pearl.oauth2.demo.entity.UserBindThirdLogin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pearl
 * @since 2023-04-23
 */
public interface IUserBindThirdLoginService extends IService<UserBindThirdLogin> {

    /**
     * 根据用户ID查询所有第三方平台绑定关系
     *
     * @param userId 用户ID
     * @return 绑定
     */
    List<UserBindThirdLogin> selectListByUserId(String userId);
}
