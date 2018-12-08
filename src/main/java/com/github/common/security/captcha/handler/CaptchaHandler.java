package com.github.common.security.captcha.handler;

import com.github.common.exception.SHException;
import com.github.common.security.captcha.CaptchaProcessor;
import com.github.common.security.captcha.support.ValidateCaptchaProcessorHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取验证码处理器
 *
 * 试了用@Component不能处理请求
 * @author ZEALER
 * @date 2018-10-25
 */
@RestController
public class CaptchaHandler {

    @Autowired
    private ValidateCaptchaProcessorHolder captchaProcessorHolder;

    /**
     * 验证码，根据验证码类型不同，调用不同的 {@link CaptchaProcessor}接口实现
     *
     * @param request
     * @param response
     * @param captchaType
     * @throws SHException
     */
    @GetMapping("/captcha/{captchaType}")
    public void createCaptcha(HttpServletRequest request, HttpServletResponse response, @PathVariable String captchaType)
            throws SHException {
        captchaProcessorHolder.findCaptchaProcessor(captchaType)
                .createCaptcha(new ServletWebRequest(request, response));
    }
}
