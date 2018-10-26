package com.github.common.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.servlet.Filter;

/**
 * 将自定义的验证码过滤器添加到SpringSecurity过滤器链中
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Component
public class ValidateCaptchaSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private Filter validateCaptchaFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCaptchaFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }


}
