package com.github.common.exception;

import com.github.common.security.exception.ValidateCaptchaException;
import com.github.common.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author ZEALER
 * @date 2018-10-20
 */
@RestControllerAdvice
public class SHExceptionHandler {


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(SHException.class)
    public ApiResponse handleSHException(SHException e){
        return ApiResponse.ofMessage(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ValidateCaptchaException.class)
    public ApiResponse handleValidateCaptchaException(ValidateCaptchaException e){
        return ApiResponse.ofMessage(ApiResponse.ResponseStatus.AUTHENTICATION_FAILED.getCode(), e.getMessage());
    }

}
