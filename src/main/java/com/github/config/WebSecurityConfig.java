package com.github.config;

import com.github.common.security.authentication.CustomAuthenticationFailureHandler;
import com.github.common.security.authentication.CustomAuthenticationSuccessHandler;
import com.github.common.security.handler.CustomLogoutSuccessHandler;
import com.github.common.security.authentication.LoginUrlEntryPoint;
import com.github.common.security.config.ValidateCaptchaSecurityConfig;
import com.github.common.security.properties.SecurityProperties;
import com.github.modules.sys.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;

/**
 * SpringSecurity 配置
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@EnableConfigurationProperties(SecurityProperties.class)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ValidateCaptchaSecurityConfig validateCaptchaSecurityConfig;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.apply(validateCaptchaSecurityConfig);

        http.authorizeRequests()
                .antMatchers("/lib/**").permitAll() // 静态资源
                .antMatchers("/captcha/*").permitAll() // 验证码
                .antMatchers("/admin/login").permitAll() // 管理员登录
                .antMatchers("/user/login").permitAll() // 普通用户登录
                .antMatchers("/admin/**").authenticated()
//                .anyRequest().authenticated() // 剩下的任何请求都需要身份认证, 问题：访问不存在的URL也会要求认证(跳转到登录页)
                .and()
                .formLogin()
                    .loginProcessingUrl("/sys/login") // 发起登录的URI
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .deleteCookies("JSESSIONID") // 删除JSESSIONID
                    .invalidateHttpSession(true) //使session失效
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

    /**
     * 登录入口
     */
    @Bean
    public LoginUrlEntryPoint urlEntryPoint() {
        // 默认为普通用户登录
        return new LoginUrlEntryPoint("/user/login");
    }

    /**
     * 加密解密类
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 操作session的工具类
     */
    @Bean
    public SessionStrategy sessionStrategy() {
        return new HttpSessionSessionStrategy();
    }

    /**
     * 认证成功处理器
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();
        successHandler.setSessionStrategy(sessionStrategy());
        return successHandler;
    }



}
