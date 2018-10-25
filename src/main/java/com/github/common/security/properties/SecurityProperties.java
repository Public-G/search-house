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

    private ValidateCaptchaProperties captcha = new ValidateCaptchaProperties();

    public ValidateCaptchaProperties getCaptcha() {
        return captcha;
    }

    public void setCaptcha(ValidateCaptchaProperties captcha) {
        this.captcha = captcha;
    }
}
