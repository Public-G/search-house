package com.github.modules.sys.dto;


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

    private int type;

    private String menuName;

    private String requestUrl;

    private String icon;

    private List<SysMenuDTO> child;

    public SysMenuDTO() {
    }

    public SysMenuDTO(Long menuId, Long parentId, int type, String menuName, String requestUrl, String icon, List<SysMenuDTO> child) {
        this.menuId = menuId;
        this.parentId = parentId;
        this.type = type;
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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public List<SysMenuDTO> getChild() {
        return child;
    }

    public void setChild(List<SysMenuDTO> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "SysMenuDTO{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", icon='" + icon + '\'' +
                ", child=" + child +
                '}';
    }
}
