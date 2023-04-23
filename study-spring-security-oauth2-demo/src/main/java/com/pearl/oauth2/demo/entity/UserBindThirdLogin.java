package com.pearl.oauth2.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author pearl
 * @since 2023-04-23
 */
@TableName("user_bind_third_login")
public class UserBindThirdLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 平台类型
     */
    private String type;

    /**
     * 用户表主键ID
     */
    private Long userId;

    /**
     * 用户在第三方平台的唯一ID
     */
    private String uuid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 第三方头像
     */
    private String headSculpture;

    /**
     * 绑定时间
     */
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getHeadSculpture() {
        return headSculpture;
    }

    public void setHeadSculpture(String headSculpture) {
        this.headSculpture = headSculpture;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserBindThirdLogin{" +
            "id=" + id +
            ", type=" + type +
            ", userId=" + userId +
            ", uuid=" + uuid +
            ", nickname=" + nickname +
            ", headSculpture=" + headSculpture +
            ", createTime=" + createTime +
        "}";
    }
}
