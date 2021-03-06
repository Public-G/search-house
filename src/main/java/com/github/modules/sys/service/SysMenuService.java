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
     * 查询所有
     */
    List<SysMenuEntity> findAll();

    /**
     * 获取用户菜单列表
     */
    List<SysMenuDTO> getUserMenuList(Long userId);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<SysMenuEntity> findMenuByParentId(Long parentId, List<Long> menuIdList);

    /**
     * 根据用户ID查询权限
     */
    List<String> findPermsByUserId(Long userId);

    /**
     * 获取除按钮意外的菜单
     * @return
     */
    List<SysMenuEntity> findIsNotBtn();

    /**
     * 保存/更新 菜单
     */
    void saveOrUpdate(SysMenuEntity sysMenuEntity);

    /**
     * 根据ID查询菜单
     * @param menuId
     * @return
     */
    SysMenuEntity findById(Long menuId);

    void deleteBatch(Long[] menuIds);
}
