package com.github.config;

import com.github.common.security.authentication.MyAuthenticationFailureHandler;
import com.github.common.security.config.LoginUrlEntryPoint;
import com.github.common.security.config.ValidateCaptchaSecurityConfig;
import com.github.common.security.properties.SecurityProperties;
import com.github.modules.sys.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity 配置
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@EnableConfigurationProperties(SecurityProperties.class)
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private ValidateCaptchaSecurityConfig validateCaptchaSecurityConfig;

    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.apply(validateCaptchaSecurityConfig);

        http.authorizeRequests()
//                .antMatchers("/static/**").permitAll() // 静态资源
//                .antMatchers("/lib/**").permitAll() // 静态资源
//                .antMatchers("/captcha/*").permitAll() // 验证码
//                .antMatchers("/admin/login").permitAll() // 管理员登录
                .antMatchers("/user/login").permitAll() // 普通用户登录
//                .antMatchers("/images/alipay.jpg").hasRole("ADMIN")
//                .anyRequest().authenticated() // 剩下的任何请求都需要身份认证, 问题：访问不存在的URL也会跳转到登录页
                .and()
                .formLogin()
                .loginProcessingUrl("/sys/login") // 发起登录的URI
                .failureHandler(authenticationFailureHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(urlEntryPoint());

        http.csrf().disable(); // 禁用csrf
        http.headers().frameOptions().sameOrigin(); // 开启同源策略
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 添加 UserDetailsService 使用的密码加密解密类
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public LoginUrlEntryPoint urlEntryPoint() {
        // 默认为普通用户登录
        return new LoginUrlEntryPoint("/user/login");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
