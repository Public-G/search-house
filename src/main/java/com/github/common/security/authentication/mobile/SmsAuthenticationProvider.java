package com.github.common.security.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 短信登录处理逻辑类
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * 身份认证的逻辑
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 获取未认证的SmsAuthenticationToken
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken)authentication;

        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        if(user == null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(user, user.getAuthorities());

        //未认证的Details设置到已经认证的SmsAuthenticationToken中去
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    /**
     * AuthenticationManager根据supports() 决定由哪个Provider处理传进来的xxxToken，xxxToken是authentication的子类
     */
    @Override
    public boolean supports(Class<?> authentication) {
        //如果调用isAssignableFrom()这个方法的class或接口 与 参数authentication表示的类或接口相同,
        // 或者是参数authentication表示的类或接口的父类，返回true
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
