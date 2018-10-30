package com.github.modules.sys.service;

import com.github.modules.sys.dto.SysMenuDTO;
import com.github.modules.sys.entity.SysMenuEntity;

import java.util.List;

/**
 * 系统菜单
 *
 * @author ZEALER
 * @date 2018-10-27
 */
public interface SysMenuService {

    /**
     * 获取用户菜单列表
     */
    List<SysMenuEntity> getUserMenuList(Long userId);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<SysMenuEntity> findMenuByParentId(Long parentId, List<Long> menuIdList);
}
