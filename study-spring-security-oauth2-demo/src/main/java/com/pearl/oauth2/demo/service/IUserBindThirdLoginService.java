package com.pearl.oauth2.demo.service;

import com.pearl.oauth2.demo.entity.UserBindThirdLogin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 * 在之前的文档中，我们完成了第三方平台登录功能，但是存在一个很大的问题。
 * 比如，某用户在我们的平台注册了账号`kunji`，当他使用第三方平台登录后，当前用户认证信息如下图所示：
 *
 * 首先，数据是比较多的，`Spring Security`默认将第三方所有的用户信息进行了保存，
 *
 * 其次当前使用第三方登录的用户信息，并不是用户在我们平台注册的用户信息，例如上面当前用户信息用户名为`pearl-organization`，
 * 用户属性都是第三方的用户信息。所以当`kunji`去使用`Gitee`登录时，当前登录用户并不是`kunji`。
 *
 * 所以我们需要将第三方平台登录用户和本地用户进行绑定，当用户使用第三方登录时，首先查询其绑定的用户，然后保存本地用户信息。
 * 这样，当前登录用户、角色、权限信息都是该用户在我们平台注册的用户信息。
 *
 * 需求说明：
 *
 * 1、
 *
 *         // 1. 登录
 *         // 1.1 Oauth2登录 发现uid有登录用户，uid查询用户信息
 *         // 1.2 Oauth2登录,请求微信、授权、重定向，查询用户信息，UID没有查到对应用户，说明未绑定，
 *         - 直接创建一个账号，并绑定，返回成功认证信息
 *         - 未绑定时，记录当前平台用户ID，重定向到注册页面，用户填写信息，注册后重新向到首页地址
 *         - 未绑定时，重定向到输入手机号，查询当前手机号的用户，存在绑定，不存在使用手机号创建用户。
 *
 *         弹出注册页面
 *
 * 2、用户绑定、解绑第三方平台
 *
 *         // 2. 登录后
 *         // 2.1 绑定，发起授权、登录、回调接收用户信息进行绑定
 *         // 2.2 解绑 直接删除
 *
 *
 *
 *
 *
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
    List<UserBindThirdLogin> selectListByUserId(Long userId);

    /**
     *  根据第三方平台类型 + 第三方用户ID 查询绑定的当前平台用户ID
     */
    UserBindThirdLogin selectOne(String type,String thirdUserId);

    /**
     *  删除绑定
     */
    void removeBind(String type,String userId);
}
