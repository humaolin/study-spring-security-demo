package com.pearl.log.demo.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pearl.log.demo.entity.LoginLog;
import com.pearl.log.demo.entity.PearlUserDetails;
import com.pearl.log.demo.entity.User;
import com.pearl.log.demo.mapper.LoginLogMapper;
import com.pearl.log.demo.service.ILoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pearl.log.demo.service.IUserService;
import com.pearl.log.demo.util.AddressUtils;
import com.pearl.log.demo.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    IUserService userService;

    //@Async // 异步执行
    @Override
    public void save(LoginLog log, HttpServletRequest request){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String requestUri = request.getRequestURI();
        // 登录时间
        log.setTime(LocalDateTime.now());
        // UA 信息
        String header = request.getHeader(Header.USER_AGENT.getValue());
        if (StrUtil.isNotEmpty(header)) {
            UserAgent userAgent = UserAgentUtil.parse(header);
            log.setIsMobile(userAgent.isMobile() ? 1 : 0);
            log.setBrowser(userAgent.getBrowser().getName());
            log.setPlatform(userAgent.getPlatform().getName());
            log.setOs(userAgent.getOs().getName());
        }
        // IP地址、归属地
        String ipAddress = IpUtils.getClientIp(request);
        log.setIp(ipAddress);
        // IP 归属地
        String realAddressByIP = AddressUtils.getRealAddressByIP(ipAddress);
        log.setLocation(realAddressByIP);
        // 登录方式：通过请求路径判断
        Map<String, String> paramMap = JakartaServletUtil.getParamMap(request);
        if (StrUtil.equals(requestUri, "/login")) {
            log.setWay("账号密码");
            // 请求参数中的用户名账号
            String username = paramMap.get("username");
            // 查询用户ID（平台用户登录后，可以查看自己的登录日志）
            if (StrUtil.isNotEmpty(username)){
                log.setAccount(username);
                User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
                if (ObjectUtil.isNotNull(user)){
                    log.setUserId(user.getUserId());
                }
            }
        } else if (StrUtil.equals(requestUri, "/login/sms")) {
            log.setWay("手机号");
            String phone = paramMap.get("phone");
            // 查询用户ID（平台用户登录后，可以查看自己的登录日志）
            if (StrUtil.isNotEmpty(phone)){
                log.setAccount(phone);
                User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
                if (ObjectUtil.isNotNull(user)){
                    log.setUserId(user.getUserId());
                }
            }
        } else if (StrUtil.startWith(requestUri, "/login/oauth2/code")) {
            // 第三方平台登录（第三方可能在当前平台没有用户ID）
            log.setWay("第三方平台");
            String s = StrUtil.subAfter(requestUri, "/", true);// 截取第三方ID
            log.setWay(s);
        } else {
            log.setWay("程序未知的认证方式");
        }
        // 直接保存到数据库 OR 调用日志服务保存 OR 发送到MQ处理
        save(log);
    }
}
