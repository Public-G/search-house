package com.github.modules.sys.service;

import com.github.modules.sys.entity.SysUserEntity;

/**
 * 系统用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
public interface SysUserService {
    SysUserEntity selectUserById(Long userId);

    SysUserEntity saveUser(SysUserEntity sysUserEntity);
}
