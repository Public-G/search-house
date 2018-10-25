package com.github.common.security.captcha.support;

import com.github.common.security.captcha.CaptchaProcessor;
import com.github.common.security.captcha.CaptchaType;
import com.github.common.security.exception.ValidateCaptchaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 验证码处理器
 * 根据 {@link CaptchaType} ，调用不同的验证码处理器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Component
public class ValidateCaptchaProcessorHolder {

    @Autowired
    private Map<String, CaptchaProcessor> captchaProcessors;

    /**
     * 根据 captchaType 获取不同的 CaptchaProcessor
     *
     * @param captchaType
     * @return
     */
    public CaptchaProcessor findCaptchaProcessor(CaptchaType captchaType) {
        return findCaptchaProcessor(captchaType.toString().toLowerCase());
    }

    public CaptchaProcessor findCaptchaProcessor(String captchaType) {
        String processorName = captchaType.toLowerCase() + CaptchaProcessor.class.getSimpleName();
        CaptchaProcessor captchaProcessor = captchaProcessors.get(processorName);
        if (captchaProcessor == null) {
            throw new ValidateCaptchaException("验证码处理器" + processorName + "不存在");
        }
        return captchaProcessor;
    }
}
