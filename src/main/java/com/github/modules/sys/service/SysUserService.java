package com.github.modules.sys.service;

import com.github.common.utils.PageUtils;
import com.github.modules.sys.entity.SysUserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
public interface SysUserService {

    PageUtils findPage(Map<String, String> params);

    /**
     * 根据用户名，查询系统用户
     */
    SysUserEntity findByUsername(String username);

    /**
     * 根据用户ID，查询系统用户
     */
    SysUserEntity findByUserId(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> findAllMenuId(Long userId);

    /**
     * 保存/修改 用户
     */
    void saveOrUpdate(SysUserEntity sysUserEntity);

    /**
     * 批量删除用户
     */
    void deleteBatch(Collection<Long> userIds);

    /**
     * 批量删除用户
     */
    void reset(Long userId);
}
