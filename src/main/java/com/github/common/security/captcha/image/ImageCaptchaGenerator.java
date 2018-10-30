package com.github.common.security.captcha.image;

import com.github.common.security.properties.SecurityProperties;
import com.github.common.security.captcha.CaptchaGenerator;
import com.github.common.security.captcha.bean.Captcha;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 图片验证码生成器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class ImageCaptchaGenerator implements CaptchaGenerator {

    private SecurityProperties securityProperties;

    @Override
    public Captcha generateCaptcha(ServletWebRequest request) {
        // 从请求参数中获取验证码的配置 > 默认配置(或者手动配置)
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width",
                securityProperties.getCaptcha().getImage().getWidth());
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height",
                securityProperties.getCaptcha().getImage().getHeight());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));

        Random random = new Random();
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }

        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < securityProperties.getCaptcha().getImage().getLength(); i++) {
            String randCaptcha = String.valueOf(random.nextInt(10));
            captcha.append(randCaptcha);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(randCaptcha, 13 * i + 6, 16);
        }

        g.dispose();

        return new ImageCaptcha(image, captcha.toString(), securityProperties.getCaptcha().getImage().getExpireIn());
    }

    /**
     * 生成随机背景条纹
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
