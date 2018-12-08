package com.github.common.security.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 封装短信登录的用户信息
 *
 * @see UsernamePasswordAuthenticationToken 参考封装用户名密码的用户信息的类
 * @author ZEALER
 * @date 2018-10-25
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 420L;

    private final Object principal;  // 认证之前是手机号，认证成功之后是用户信息

    public SmsAuthenticationToken(String mobile) {
        super((Collection) null);
        this.principal = mobile;
        this.setAuthenticated(false);
    }

    public SmsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}