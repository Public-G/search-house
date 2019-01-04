package com.github.modules.sys.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.base.service.BaseService;
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
public interface SysUserService extends BaseService<SysUserEntity> {

//    PageUtils findPage(PageForm pageForm);

//    /**
//     * 根据用户名，查询系统用户
//     */
//    SysUserEntity findByUsername(String username);
//
//    /**
//     * 根据用户ID，查询系统用户
//     */
//    SysUserEntity findByUserId(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> findAllMenuId(Long userId);

//    /**
//     * 保存用户
//     */
//    void save(SysUserEntity sysUserEntity);

//    /**
//     * 修改用户
//     */
//    void update(SysUserEntity sysUserEntity);
//
//    /**
//     * 批量删除用户
//     */
//    void deleteBatch(Long[] userIds);

    /**
     * 重置密码
     */
    void reset(Long userId);
}
