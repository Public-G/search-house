package com.github.entity;

import com.github.SearchHouseApplicationTests;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.repository.SysUserRepository;
import com.github.modules.sys.service.SysUserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class SysUserRepositoryTest extends SearchHouseApplicationTests{

    @Autowired
    SysUserRepository sysUserRepository;

    @Autowired
    SysUserService sysUserService;

    @Test
    public void testFindOne() {
        SysUserEntity userEntity = sysUserRepository.findOne(1L);
        Assert.assertEquals("张三", userEntity.getUsername());
    }

    @Test
    public void testSaveOne() {
//        SysUserEntity userEntity = new SysUserEntity(2L, "李四", "123",
//                "", "", 0, 1L, new Date(), new Date());
//        SysUserEntity sysUserEntity = sysUserService.saveUser(userEntity);
//        Assert.assertEquals(sysUserEntity.getUserId(), userEntity.getUserId());
    }
}
