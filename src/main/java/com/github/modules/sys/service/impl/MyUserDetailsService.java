package com.github.modules.sys.service.impl;

import com.github.common.constant.SysConstant;
import com.github.common.security.exception.ValidateCaptchaException;
import com.github.modules.sys.entity.SysMenuEntity;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.repository.SysMenuRepository;
import com.github.modules.sys.repository.SysUserRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * SpringSecurity相关接口
 *
 * @author ZEALER
 * @date 2018-10-26
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("用户"+ username +"登录");

        // 根据用户名查找用户信息
        SysUserEntity sysUserEntity = sysUserRepository.findByUsername(username);
        if (sysUserEntity == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
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
            List<SysMenuEntity> menuList = sysMenuRepository.findAll();
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysMenuRepository.findPermsByUserId(userId);
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
