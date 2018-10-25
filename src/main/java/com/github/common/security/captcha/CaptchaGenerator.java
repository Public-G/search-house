package com.github.common.security.captcha;

import com.github.common.security.captcha.bean.Captcha;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public interface CaptchaGenerator {

    /**
     * 生成验证码
     * @param request 用于包装 HttpServletRequest 和 HttpServletResponse
     */
    Captcha generateCaptcha(ServletWebRequest request);
}
