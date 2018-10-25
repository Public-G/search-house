package com.github.common.security.properties;

/**
 * 验证码Properties配置
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class ValidateCaptchaProperties {

    private ImageCaptchaProperties image = new ImageCaptchaProperties();

    private SmsCaptchaProperties sms = new SmsCaptchaProperties();

    public ImageCaptchaProperties getImage() {
        return image;
    }

    public void setImage(ImageCaptchaProperties image) {
        this.image = image;
    }

    public SmsCaptchaProperties getSms() {
        return sms;
    }

    public void setSms(SmsCaptchaProperties sms) {
        this.sms = sms;
    }
}
