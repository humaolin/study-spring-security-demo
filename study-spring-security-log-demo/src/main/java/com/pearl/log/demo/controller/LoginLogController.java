package com.pearl.log.demo.controller;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.pearl.log.demo.entity.LoginLog;
import com.pearl.log.demo.service.ILoginLogService;
import com.pearl.log.demo.util.AddressUtils;
import com.pearl.log.demo.util.IpUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 系统访问记录 前端控制器
 * </p>
 *
 * @author pearl
 * @since 2023-06-26
 */
@RestController
@RequestMapping("/loginLog")
public class LoginLogController {

    @Resource
    ILoginLogService loginLogService;

    @GetMapping("/list")
    public List<LoginLog> list() {
        return loginLogService.list();
    }

    @GetMapping("/test")
    public Object test(HttpServletRequest request) {
        // Hutool工具获取UA
        String header = request.getHeader(Header.USER_AGENT.getValue());
        UserAgent userAgent = UserAgentUtil.parse(header);
        // IP地址
        String ipAddress = IpUtils.getClientIp(request);
        String remoteAddr = request.getRemoteAddr();
        // IP 归属地
        AddressUtils.getRealAddressByIP("116.179.32.42");
        return userAgent;
    }
}
