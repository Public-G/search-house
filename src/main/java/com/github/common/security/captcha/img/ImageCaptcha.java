package com.github.common.security.captcha.img;

import com.github.common.security.captcha.bean.Captcha;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图片验证码
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class ImageCaptcha extends Captcha {
    private static final long serialVersionUID = 17653452314123412L;

    private BufferedImage image;

    public ImageCaptcha(BufferedImage image, String captcha, int expireSecondIn) {
        super(captcha, expireSecondIn);
        this.image = image;
    }

    public ImageCaptcha(BufferedImage image, String captcha, LocalDateTime expireTime) {
        super(captcha, expireTime);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
