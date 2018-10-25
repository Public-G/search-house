package com.github.common.security.captcha.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码基类
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class Captcha implements Serializable {
    private static final long serialVersionUID = 1765342344512334123L;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 判断当前时间是否是在expireTime之后
     */
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public Captcha(String captcha, int expireSecondIn) {
        this.captcha = captcha;
        this.expireTime = LocalDateTime.now().plusSeconds(expireSecondIn); //指定过期秒数
    }

    public Captcha(String captcha, LocalDateTime expireTime) {
        this.captcha = captcha;
        this.expireTime = expireTime;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
