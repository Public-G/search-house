package com.github.common.security.config;

import com.github.common.security.captcha.CaptchaGenerator;
import com.github.common.security.captcha.image.ImageCaptchaGenerator;
import com.github.common.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码生成器配置类
 * 如果系统中没有自实现生成验证码逻辑，则使用默认的
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Configuration
public class CaptchaBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(ImageCaptchaGenerator.class)
    public CaptchaGenerator imageCaptchaGenerator() {
        ImageCaptchaGenerator imageCaptchaGenerator = new ImageCaptchaGenerator();
        imageCaptchaGenerator.setSecurityProperties(securityProperties);
        return imageCaptchaGenerator;
    }
}
