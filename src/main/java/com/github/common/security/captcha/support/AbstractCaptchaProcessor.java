package com.github.common.security.captcha.support;

import com.github.common.security.exception.ValidateCaptchaException;
import com.github.common.security.captcha.CaptchaGenerator;
import com.github.common.security.captcha.CaptchaProcessor;
import com.github.common.security.captcha.CaptchaType;
import com.github.common.security.captcha.bean.Captcha;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.xml.bind.ValidationException;
import java.util.Map;

/**
 * 验证码处理器(生成、发送、校验)抽象类
 * 根据 {@link CaptchaType} ，调用不同的验证码生成器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public abstract class AbstractCaptchaProcessor<C extends Captcha> implements CaptchaProcessor {

    /**
     * 操作session的工具类
     */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 收集系统中所有的 {@link CaptchaGenerator} 接口的实现
     */
    @Autowired
    private Map<String, CaptchaGenerator> captchaGenerators;

    /**
     * 生成、保存、发送验证码
     *
     * @param servletWebRequest：
     * @throws ValidationException
     */
    @Override
    public void createCaptcha(ServletWebRequest servletWebRequest) throws ValidationException {
        C captcha = generateCaptcha(servletWebRequest);
        saveCaptcha(servletWebRequest, captcha);
        sendCaptcha(servletWebRequest, captcha);
    }

    /**
     * 发送校验码，由子类实现
     *
     * @param request
     * @param captcha
     * @throws Exception
     */
    protected abstract void sendCaptcha(ServletWebRequest request, C captcha);

    /**
     * 生成校验码
     * 根据不同的生成器，调用不同的验证码生成方法
     *
     * @param request
     * @return
     */
    private C generateCaptcha(ServletWebRequest request) {
        String type = getCaptchaType(request).toString().toLowerCase();

        // 根据处理器类名的前缀获取对应的处理器
        String generatorName = type + CaptchaGenerator.class.getSimpleName();
        CaptchaGenerator captchaGenerator = captchaGenerators.get(generatorName);
        if (captchaGenerator == null) {
            throw new ValidateCaptchaException("验证码生成器" + generatorName + "不存在");
        }
        return (C) captchaGenerator.generateCaptcha(request);
    }

    /**
     * 获取处理器类名的前缀，例如：ImageCaptchaProcessor --> Image
     *
     * @param request
     * @return
     */
    private CaptchaType getCaptchaType(ServletWebRequest request) {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CaptchaProcessor");

        //返回枚举类对象
        return CaptchaType.valueOf(type.toUpperCase());
    }

    /**
     * 保存验证码
     *
     * @param request
     * @param validateCaptcha
     */
    private void saveCaptcha(ServletWebRequest request, C validateCaptcha) {
        // Session放到redis存储，ImageCode中的BufferedImage不能实现序列号接口
        // 但同时Image也不需要放到session中存储(放入验证码和过期时间就行)，这里重新包装下
        Captcha captcha = new Captcha(validateCaptcha.getCaptcha(), validateCaptcha.getExpireTime());
        sessionStrategy.setAttribute(request, getSessionKey(request), captcha);
    }

    /**
     * 构建验证码放入 session 时的 key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getCaptchaType(request).toString().toUpperCase();
    }

    /**
     * 验证码校验
     *
     * @param request
     */
    public void validateCaptcha(ServletWebRequest request){
        CaptchaType processorType = getCaptchaType(request);

        //从session中获取验证码类型的key：SESSION_KEY_FOR_CODE__SMS
        String sessionKey = getSessionKey(request);

        //从请求参数中获取输入的验证码
        C captchaInSession = (C) sessionStrategy.getAttribute(request, sessionKey);

        String captchaInRequest;
        try {
            //从请求中获取 http请求中默认的携带 图片/短信 验证码信息的参数的名称
            captchaInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCaptchaException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(captchaInRequest)) {
            throw new ValidateCaptchaException(processorType + "验证码的值不能为空");
        }

        if (captchaInSession == null) {
            throw new ValidateCaptchaException(processorType + "验证码不存在");
        }

        if (captchaInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, sessionKey);
            throw new ValidateCaptchaException(processorType + "验证码已过期");
        }

        if (!StringUtils.equals(captchaInSession.getCaptcha(), captchaInRequest)) {
            throw new ValidateCaptchaException(processorType + "验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, sessionKey);
    }
}
