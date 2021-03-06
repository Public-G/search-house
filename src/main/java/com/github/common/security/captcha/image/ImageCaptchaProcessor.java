package com.github.common.security.captcha.image;

import com.github.common.exception.SHException;
import com.github.common.security.captcha.support.AbstractCaptchaProcessor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.xml.bind.ValidationException;
import java.io.IOException;


/**
 * 图片验证码处理器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Component
public class ImageCaptchaProcessor extends AbstractCaptchaProcessor<ImageCaptcha> {

    /**
     * 发送图形验证码，将其写到响应中
     *
     * @param request
     * @param imageCaptcha
     */
    @Override
    protected void sendCaptcha(ServletWebRequest request, ImageCaptcha imageCaptcha) {
        ServletOutputStream outputStream = null;
        try {
            outputStream = request.getResponse().getOutputStream();
            ImageIO.write(imageCaptcha.getImage(), "JPEG", outputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new SHException("发送图片验证码失败，请稍后重试");
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
}
