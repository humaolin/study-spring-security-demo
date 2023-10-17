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
import com.pearl.log.demo.entity.RequestDTO;
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

    @Async // 异步执行
    @Override
    public void save(RequestDTO requestDTO, boolean isSuccess, String msg) {
        LoginLog loginLog = new LoginLog();
        // 1. UA 信息
        String userAgentHeader = requestDTO.getUserAgent();
        if (StrUtil.isNotEmpty(userAgentHeader)) {
            UserAgent userAgent = UserAgentUtil.parse(userAgentHeader);
            loginLog.setIsMobile(userAgent.isMobile() ? 1 : 0);
            loginLog.setBrowser(userAgent.getBrowser().getName());
            loginLog.setPlatform(userAgent.getPlatform().getName());
            loginLog.setOs(userAgent.getOs().getName());
        }
        // 2. IP地址、IP 归属地
        String ipAddress = requestDTO.getIpAddress();
        loginLog.setIp(ipAddress);
        String realAddressByIP = AddressUtils.getRealAddressByIP(ipAddress);
        loginLog.setLocation(realAddressByIP);
        // 3. 登录方式：通过请求路径判断
        Map<String, String> paramMap = requestDTO.getParamMap();
        String requestUri = requestDTO.getRequestUri();
        if (StrUtil.equals(requestUri, "/login")) {
            loginLog.setWay("账号密码");
            // 请求参数中的用户名账号
            String username = paramMap.get("username");
            // 查询用户ID（平台用户登录后，可以查看自己的登录日志）
            if (StrUtil.isNotEmpty(username)) {
                loginLog.setAccount(username);
                User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
                if (ObjectUtil.isNotNull(user)) {
                    loginLog.setUserId(user.getUserId());
                }
            }
        } else if (StrUtil.equals(requestUri, "/login/sms")) {
            loginLog.setWay("手机号");
            String phone = paramMap.get("phone");
            // 查询用户ID（平台用户登录后，可以查看自己的登录日志）
            if (StrUtil.isNotEmpty(phone)) {
                loginLog.setAccount(phone);
                User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
                if (ObjectUtil.isNotNull(user)) {
                    loginLog.setUserId(user.getUserId());
                }
            }
        } else if (StrUtil.startWith(requestUri, "/login/oauth2/code")) {
            // 第三方平台登录（第三方可能在当前平台没有用户ID）
            loginLog.setWay(StrUtil.subAfter(requestUri, "/", true));// 截取第三方ID
        }
        // 4. 其他
        loginLog.setTime(LocalDateTime.now());// 登录时间
        loginLog.setMsg(msg);// 提示消息
        loginLog.setStatus(isSuccess ? 1 : 0);
        // 5. 直接保存到数据库 OR 调用日志服务保存 OR 发送到MQ处理
        save(loginLog);
    }

    @Override
    public RequestDTO getRequestDTO(HttpServletRequest request) {
        // 获取HttpServletRequest中，登录日志需要的相关属性，方便异步执行
        String requestUri = request.getRequestURI();
        String userAgent = request.getHeader(Header.USER_AGENT.getValue());
        String ipAddress = IpUtils.getClientIp(request);
        Map<String, String> paramMap = JakartaServletUtil.getParamMap(request);
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRequestUri(requestUri);
        requestDTO.setIpAddress(ipAddress);
        requestDTO.setUserAgent(userAgent);
        requestDTO.setParamMap(paramMap);
        return requestDTO;
    }
}
