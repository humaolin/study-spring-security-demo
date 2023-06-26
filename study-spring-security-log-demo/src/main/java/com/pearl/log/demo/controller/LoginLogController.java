package com.pearl.log.demo.controller;

import com.pearl.log.demo.entity.LoginLog;
import com.pearl.log.demo.service.ILoginLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 系统访问记录 前端控制器
 * </p>
 *
 * @author pearl
 * @since 2023-06-26
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController {

    @Resource
    ILoginLogService loginLogService;

    @GetMapping
    public List<LoginLog> list() {
        return loginLogService.list();
    }
}
