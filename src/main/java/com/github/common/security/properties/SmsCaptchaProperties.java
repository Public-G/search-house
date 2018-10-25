package com.github.common.security.properties;

/**
 * 短信验证码Properties配置
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class SmsCaptchaProperties {

    private int length = 6;

    private int expireIn = 60;

    private String url;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
