package com.github.modules.sys.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Table(name = "sys_user")
@Entity
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1432534534L;

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String username;

    private String password;

    private String email;

    private String mobile;

    /**
     * 状态  0：正常   1：禁用
     */
    private Integer status;

    @Column(name = "create_user_id")
    private Long createUserId;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    public SysUserEntity() {
    }

    public SysUserEntity(Long userId, String username, String password, String email, String mobile, Integer status, Long createUserId, Date createTime, Date lastLoginTime) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
        this.createUserId = createUserId;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
