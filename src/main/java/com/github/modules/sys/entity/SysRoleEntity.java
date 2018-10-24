package com.github.modules.sys.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统角色
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Table(name = "sys_role")
@Entity
public class SysRoleEntity implements Serializable {
    private static final long serialVersionUID = 14376534534L;

    @Id
    private Long roleId;

    @Column(name = "role_name")
    private String roleName;

    private String remark;

    @Column(name = "create_user_id")
    private Long createUserId;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public SysRoleEntity() {
    }

    public SysRoleEntity(Long roleId, String roleName, String remark, Long createUserId, Date createTime) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.remark = remark;
        this.createUserId = createUserId;
        this.createTime = createTime;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
