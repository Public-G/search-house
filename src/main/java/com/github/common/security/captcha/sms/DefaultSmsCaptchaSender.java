package com.github.common.security.captcha.sms;

import com.github.common.exception.SHException;
import com.github.common.security.properties.SecurityProperties;
import com.github.common.utils.HttpClientUtils;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * 默认的发送短信验证码类
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class DefaultSmsCaptchaSender implements SmsCaptchaSender {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private SecurityProperties securityProperties;

    /**
     * 短信应用SDK AppID
     */
    @Value("${qcloudsms.appid}")
    private Integer appid;

    /**
     * 短信应用SDK AppKey
     */
    @Value("${qcloudsms.appkey}")
    private String appkey;

    /**
     * 短信模板ID，需要在短信应用中申请
     */
    @Value("${qcloudsms.templateId}")
    private Integer templateId;

    /**
     * 指定模板ID单发短信(腾讯云短信)
     *
     * @param mobile
     * @param code
     */
    @Override
    public void send(String mobile, String code) {
        // 短信有效期
        String smsExpireIn = String.valueOf(securityProperties.getCaptcha().getSms().getExpireIn());
        try {
            String[]        params  = {code, smsExpireIn}; // 模板参数
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", mobile,
                    templateId, params, "", "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            if (result.result != 0) {
                logger.warn("发送短信验证码失败，错误码：{}，错误消息：{}", result.result, result.errMsg);
                throw new SHException("发送短信验证码失败，请稍后重试");
            }
        } catch (HTTPException | JSONException | IOException e) {
            logger.error("发送短信验证码出现异常", e);
            throw new SHException("发送短信验证码失败，请稍后重试");
        }
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
