package com.pearl.log.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统访问记录
 * </p>
 *
 * @author pearl
 * @since 2023-06-27
 */
@TableName("login_log")
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 登录方式
     */
    private String way;

    /**
     * 登录时间
     */
    private LocalDateTime time;

    /**
     * 登录IP地址
     */
    private String ip;

    /**
     * IP归属地
     */
    private String location;

    /**
     * 登录状态（0成功 1失败）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 是否移动终端（1是0否）
     */
    private Integer isMobile;

    /**
     * 浏览器及版本
     */
    private String browser;

    /**
     * 平台类型（Windows、iPhone、Android等）
     */
    private String platform;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 账号（可能是用户名、手机号、邮箱、第三方平台账号等）
     */
    private String account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
    public Integer getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(Integer isMobile) {
        this.isMobile = isMobile;
    }
    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
            "id=" + id +
            ", userId=" + userId +
            ", way=" + way +
            ", time=" + time +
            ", ip=" + ip +
            ", location=" + location +
            ", status=" + status +
            ", remark=" + remark +
            ", os=" + os +
            ", isMobile=" + isMobile +
            ", browser=" + browser +
            ", platform=" + platform +
            ", msg=" + msg +
            ", account=" + account +
        "}";
    }
}
