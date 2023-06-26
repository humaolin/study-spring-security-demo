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
 * @since 2023-06-26
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
     * 昵称
     */
    private String nickName;

    /**
     * 登录方式
     */
    private Integer way;

    /**
     * 登录时间
     */
    private LocalDateTime time;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录IP地址
     */
    private String ip;

    /**
     * 地理位置
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
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
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

    @Override
    public String toString() {
        return "LoginLog{" +
            "id=" + id +
            ", userId=" + userId +
            ", nickName=" + nickName +
            ", way=" + way +
            ", time=" + time +
            ", userAgent=" + userAgent +
            ", os=" + os +
            ", ip=" + ip +
            ", location=" + location +
            ", status=" + status +
            ", remark=" + remark +
        "}";
    }
}
