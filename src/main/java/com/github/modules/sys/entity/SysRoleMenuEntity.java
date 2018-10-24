package com.github.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 角色与菜单对应关系
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Table(name = "sys_role_menu")
@Entity
public class SysRoleMenuEntity implements Serializable {
    private static final long serialVersionUID = 4545431534132L;

    @Id
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "menu_id")
    private Long menuId;

    public SysRoleMenuEntity() {
    }

    public SysRoleMenuEntity(Long id, Long roleId, Long menuId) {
        this.id = id;
        this.roleId = roleId;
        this.menuId = menuId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
