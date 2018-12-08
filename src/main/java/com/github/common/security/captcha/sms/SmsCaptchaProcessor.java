package com.github.common.security.captcha.sms;

import com.github.common.exception.SHException;
import com.github.common.security.captcha.bean.Captcha;
import com.github.common.security.captcha.support.AbstractCaptchaProcessor;
import com.github.common.security.constant.SecurityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;


/**
 * 短信验证码处理器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Component
public class SmsCaptchaProcessor extends AbstractCaptchaProcessor<Captcha> {

    @Autowired
    private SmsCaptchaSender smsCodeSender;

    /**
     * 发送短信验证码
     *
     * @see SmsCaptchaSender#send
     * @param request
     * @param captcha
     */
    @Override
    protected void sendCaptcha(ServletWebRequest request, Captcha captcha) {
        String paramName = SecurityConstant.DEFAULT_PARAMETER_NAME_MOBILE;

        //请求参数中一定要有 name = SecurityConstant.DEFAULT_PARAMETER_NAME_CODE_SMS
        String mobile = null;
        try {
            mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
        } catch (ServletRequestBindingException e) {
            logger.error(e.getMessage(), e);
            throw new SHException("无法获取手机号");
        }
        smsCodeSender.send(mobile, captcha.getCaptcha());
    }
}
