package com.github.modules.sys.service;

import com.github.modules.sys.entity.SysUserEntity;

/**
 * 系统用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
public interface SysUserService {

    /**
     * 根据用户名，查询系统用户
     */
    SysUserEntity findByUsername(String username);

    SysUserEntity selectUserById(Long userId);

    SysUserEntity saveUser(SysUserEntity sysUserEntity);
}
