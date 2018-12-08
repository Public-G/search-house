package com.github.modules.sys.service.impl;

import com.github.common.constant.SysConstant;
import com.github.modules.sys.entity.SysMenuEntity;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.service.SysMenuService;
import com.github.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SpringSecurity相关接口
 *
 * @author ZEALER
 * @date 2018-10-26
 */
@Service("sysUserDetailsService")
public class CustomSysUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomSysUserDetailsService.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("用户"+ username +"登录");

        // 根据用户名查找用户信息
        SysUserEntity sysUserEntity = sysUserService.findByUsername(username);
        if (sysUserEntity == null) {
            // catch异常处默认隐藏UsernameNotFoundException, 最终以BadCredentialsException抛出
            throw new UsernameNotFoundException("");
        }

        // 设置用户权限
        Set<GrantedAuthority> userAuthorities = getUserPermissions(sysUserEntity.getUserId());
        sysUserEntity.setAuthorities(userAuthorities);

        // 交由 AbstractUserDetailsAuthenticationProvider 作后续验证
        return sysUserEntity;
    }

    /**
     * 获取用户权限列表
     */
    private Set<GrantedAuthority> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == SysConstant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuService.findAll();
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysMenuService.findPermsByUserId(userId);
        }

        //用户权限列表
        Set<GrantedAuthority> permsSet = new HashSet<>();
        List<GrantedAuthority> authorityList;
        for(String perm : permsList){
            if(StringUtils.isBlank(perm)){
                continue;
            }
            authorityList = AuthorityUtils.createAuthorityList(perm.trim().split("[，,|]+"));
            permsSet.addAll(authorityList);
        }

        // 包装一下
        return Collections.unmodifiableSet(permsSet);
    }
}
