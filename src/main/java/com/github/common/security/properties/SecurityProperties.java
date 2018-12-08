package com.github.common.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringSecurity全局Properties配置
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@ConfigurationProperties(prefix = "search.house.security")
public class SecurityProperties {

    private CaptchaProperties captcha = new CaptchaProperties();

    public CaptchaProperties getCaptcha() {
        return captcha;
    }

    public void setCaptcha(CaptchaProperties captcha) {
        this.captcha = captcha;
    }
}
