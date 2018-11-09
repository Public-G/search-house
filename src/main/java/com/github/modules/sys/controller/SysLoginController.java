package com.github.modules.sys.controller;

import com.github.common.security.captcha.CaptchaProcessor;
import com.github.common.security.captcha.support.ValidateCaptchaProcessorHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

/**
 * 登录相关
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Controller
public class SysLoginController {

    @Autowired
    private ValidateCaptchaProcessorHolder captchaProcessorHolder;

    /**
     * 验证码，根据验证码类型不同，调用不同的 {@link CaptchaProcessor}接口实现
     *
     * @param request
     * @param response
     * @param captchaType
     * @throws ValidationException
     */
    @GetMapping("/captcha/{captchaType}")
    public void createCaptcha(HttpServletRequest request, HttpServletResponse response, @PathVariable String captchaType)
            throws ValidationException {
        captchaProcessorHolder.findCaptchaProcessor(captchaType)
                .createCaptcha(new ServletWebRequest(request, response));
    }
}
