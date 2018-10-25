package com.github.common.security.properties;

/**
 * 图片验证码Properties配置
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class ImageCaptchaProperties extends SmsCaptchaProperties{

    private int width = 67;

    private int height = 23;

    /**
     * 设置默认长度
     */
    public ImageCaptchaProperties(){
        setLength(4);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
