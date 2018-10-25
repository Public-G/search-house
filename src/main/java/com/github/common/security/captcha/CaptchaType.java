package com.github.common.security.captcha;


import com.github.common.security.constant.SecurityConstant;

/**
 * 验证码枚举类
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public enum CaptchaType {

    /**
     * 短信验证码
     */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstant.DEFAULT_PARAMETER_NAME_CODE_SMS;
        }
    },

    /**
     * 图片验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstant.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
        }
    };

    /**
     * 校验时从请求中获取的参数的名字
     */
    public abstract String getParamNameOnValidate();
}
