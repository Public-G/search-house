package com.github.common.security.captcha.img;

import com.github.common.security.captcha.support.AbstractCaptchaProcessor;
import com.github.common.security.exception.ValidateCaptchaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
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

    Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 发送图形验证码，将其写到响应中
     * @param request
     * @param imageCaptcha
     * @throws ValidationException
     */
    @Override
    protected void sendCaptcha(ServletWebRequest request, ImageCaptcha imageCaptcha) {
        try {
            ImageIO.write(imageCaptcha.getImage(), "JPEG", request.getResponse().getOutputStream());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new ValidateCaptchaException("发送验证码失败");
        }
    }
}
