package com.github.modules.search.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 普通用户
 *
 * @author ZEALER
 * @date 2018-12-06
 */
@Table(name = "tb_user")
@Entity
public class UserEntity implements UserDetails, Serializable {

    private static final long serialVersionUID = 1109221534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String mobile;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createTime;

    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @Transient
    private Collection<? extends GrantedAuthority>  authorities;

    public UserEntity() {
    }

    public UserEntity(String mobile, Date createTime, Date lastLoginTime) {
        this.mobile = mobile;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    /**
     * 权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return getMobile();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
