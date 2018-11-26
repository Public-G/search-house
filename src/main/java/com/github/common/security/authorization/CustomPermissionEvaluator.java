package com.github.common.security.authorization;

import com.github.common.constant.SysConstant;
import com.github.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * 自定义 hasPermission() 表达式授权逻辑
 *
 * @author ZEALER
 * @date 2018-10-29
 */
@Configuration
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        Object principal = authentication.getPrincipal();

        boolean hasPermission = false;

        if (principal instanceof SysUserEntity) {
            SysUserEntity sysUserEntity = (SysUserEntity) principal;
            //系统管理员，拥有最高权限
            if (Objects.equals(SysConstant.SUPER_ADMIN, sysUserEntity.getUserId())) {
                hasPermission = true;
            } else {
                Collection<? extends GrantedAuthority> authorities = sysUserEntity.getAuthorities();
                for (GrantedAuthority authority : authorities) {
                    if (StringUtils.equals(authority.getAuthority(), String.valueOf(permission))) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        }

        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
