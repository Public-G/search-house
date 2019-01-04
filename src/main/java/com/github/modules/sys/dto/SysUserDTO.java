package com.github.modules.sys.dto;

import java.io.Serializable;
import java.util.Date;

public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 21353453L;

    private Long userId;

    private String username;

    private String email;

    private String mobile;

    private Integer status;

    private String createUserName;

    private Date createTime;

    private Date lastLoginTime;

    public SysUserDTO() {
    }

    public SysUserDTO(Long userId, String username, String email, String mobile, Integer status, Date createTime, Date lastLoginTime) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.status = status;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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
}
