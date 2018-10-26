package com.github.modules.sys.service.impl;

import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.repository.SysUserRepository;
import com.github.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public SysUserEntity findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }

    @Override
    public SysUserEntity selectUserById(Long userId) {
        return sysUserRepository.findOne(userId);
    }

    @Transactional
    @Override
    public SysUserEntity saveUser(SysUserEntity sysUserEntity) {
        SysUserEntity userEntity = sysUserRepository.save(sysUserEntity);
         int i = 10 / 0;
        return userEntity;
    }
}
