package com.github.common.security.constant;

/**
 * SpringSecurity配置常量
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public interface SecurityConstant {

    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCaptcha";

    /**
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCaptcha";

    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";

    /**
     * 默认的用户名密码登录请求处理url
     */
    public static final String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/sys/login";

    /**
     * 默认的手机验证码登录请求处理url
     */
    public static final String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/user/auth/login";
}
