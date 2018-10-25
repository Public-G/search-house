package com.github.common.security.captcha;

import com.github.common.exception.SHException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.xml.bind.ValidationException;

/**
 * 验证码处理器，封装不同验证码的处理逻辑
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public interface CaptchaProcessor {

    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 创建验证码
     *
     * @param servletWebRequest：用于包装 HttpServletRequest 和 HttpServletResponse
     * @throws SHException
     */
    void createCaptcha(ServletWebRequest servletWebRequest) throws ValidationException;

    /**
     * 校验验证码
     *
     * @param servletWebRequest
     */
    void validateCaptcha(ServletWebRequest servletWebRequest);
}
