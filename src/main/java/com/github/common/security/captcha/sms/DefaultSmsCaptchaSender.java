package com.github.common.security.captcha.sms;

/**
 * 默认的发送短信验证码类
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class DefaultSmsCaptchaSender implements SmsCaptchaSender {

    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机" + mobile + "发送短信验证码" + code);
    }
}
