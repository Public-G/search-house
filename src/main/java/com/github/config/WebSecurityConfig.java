package com.github.config;

import com.github.common.security.authentication.LoginUrlEntryPoint;
import com.github.common.security.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {

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
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(urlEntryPoint());

        http.csrf().disable(); // 禁用csrf
        http.headers().frameOptions().sameOrigin(); // 开启同源策略
    }

    @Bean
    public LoginUrlEntryPoint urlEntryPoint() {
        // 默认为普通用户登录
        return new LoginUrlEntryPoint("/user/login");
    }
}
