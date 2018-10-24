package com.github.modules.sys.entity;

import javax.persistence.*;
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
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "request_url")
    private String requestUrl;

    /**
     * 类型   0：顶级目录  1：目录  2：菜单  3：按钮
     */
    private Integer type;

    private String icon;

    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 父菜单名称
     */
    @Transient
    private String parentName;

    @Transient
    private List<SysMenuEntity> child;

    public SysMenuEntity() {
    }

    public SysMenuEntity(Long menuId, Long parentId, String menuName, String requestUrl, Integer type, String icon, Integer orderNum, String parentName, List<SysMenuEntity> child) {
        this.menuId = menuId;
        this.parentId = parentId;
        this.menuName = menuName;
        this.requestUrl = requestUrl;
        this.type = type;
        this.icon = icon;
        this.orderNum = orderNum;
        this.parentName = parentName;
        this.child = child;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysMenuEntity> getChild() {
        return child;
    }

    public void setChild(List<SysMenuEntity> child) {
        this.child = child;
    }
}
