package com.github.modules.sys.dto;

import com.github.modules.sys.entity.SysMenuEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 系统菜单DTO
 *
 * @author ZEALER
 * @date 2018-10-27
 */
public class SysMenuDTO implements Serializable{
    private static final long serialVersionUID = 621653453L;

    private Long menuId;

    private Long parentId;

    private String menuName;

    private String requestUrl;

    private String icon;

    private List<SysMenuEntity> child;

    public SysMenuDTO() {
    }

    public SysMenuDTO(Long menuId, Long parentId, String menuName, String requestUrl, String icon, List<SysMenuEntity> child) {
        this.menuId = menuId;
        this.parentId = parentId;
        this.menuName = menuName;
        this.requestUrl = requestUrl;
        this.icon = icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SysMenuEntity> getChild() {
        return child;
    }

    public void setChild(List<SysMenuEntity> child) {
        this.child = child;
    }
}
