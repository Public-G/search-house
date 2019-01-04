package com.github.modules.sys.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 系统菜单
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Table(name = "sys_menu")
@Entity
public class SysMenuEntity implements Serializable {
    private static final long serialVersionUID = 4216534534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "parent_id")
    @NotNull(message = "父菜单不能为空")
    private Long parentId;

    @Column(name = "menu_name")
    @NotBlank(message = "菜单名不能为空")
    private String menuName;

    @Column(name = "request_url")
    private String requestUrl;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型   0：顶级目录  1：目录  2：菜单  3：按钮
     */
    @NotNull(message = "类型不能为空")
    private Integer type;

    private String icon;

    @Column(name = "order_num")
    private Integer orderNum;

    public SysMenuEntity() {
    }

    public SysMenuEntity(Long menuId, Long parentId, String menuName, String requestUrl, String perms, Integer type, String icon, Integer orderNum) {
        this.menuId = menuId;
        this.parentId = parentId;
        this.menuName = menuName;
        this.requestUrl = requestUrl;
        this.perms = perms;
        this.type = type;
        this.icon = icon;
        this.orderNum = orderNum;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
