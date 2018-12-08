package com.github.common.security.captcha.sms;

import com.github.common.security.captcha.CaptchaGenerator;
import com.github.common.security.captcha.bean.Captcha;
import com.github.common.security.properties.SecurityProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 手机验证码生成器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Component
public class SmsCaptchaGenerator implements CaptchaGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public Captcha generateCaptcha(ServletWebRequest request) {
        // 随机生成指定位数验证码
        String captcha = RandomStringUtils.randomNumeric(securityProperties.getCaptcha().getSms().getLength());

        return new Captcha(captcha, securityProperties.getCaptcha().getSms().getExpireIn());
    }
}
