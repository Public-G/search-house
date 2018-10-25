package com.github.common.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * 自定义认证授权过程中的异常
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class ValidateCaptchaException extends AuthenticationException {

    public ValidateCaptchaException(String msg) {
        super(msg);
    }
}
