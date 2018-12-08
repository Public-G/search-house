package com.github.common.security.captcha.sms;

import org.springframework.stereotype.Component;

/**
 * 短信验证码发送接口
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public interface SmsCaptchaSender {

    void send(String mobile, String code);
}
