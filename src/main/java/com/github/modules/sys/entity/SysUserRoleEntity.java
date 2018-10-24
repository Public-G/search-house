package com.github.modules.sys.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户与角色对应关系
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Table(name = "sys_user_role")
@Entity
public class SysUserRoleEntity implements Serializable {
    private static final long serialVersionUID = 4216534132L;

    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    public SysUserRoleEntity() {
    }

    public SysUserRoleEntity(Long id, Long userId, Long roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
