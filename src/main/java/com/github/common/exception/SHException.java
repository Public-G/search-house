package com.github.common.exception;

import com.github.common.utils.ApiStrategy;

/**
 * 自定义异常
 *
 * @author ZEALER
 * @date 2018-10-20
 */
public class SHException extends RuntimeException {

    private static final long serialVersionUID = 16546543763L;

    private int code = 500;
    private String msg;
    private ApiStrategy apiStrategy = ApiStrategy.FORWARD;


    public SHException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SHException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SHException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public SHException(int code, String msg, ApiStrategy apiStrategy) {
        super(msg);
        this.msg = msg;
        this.code = code;
        this.apiStrategy = apiStrategy;
    }

    public SHException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ApiStrategy getApiStrategy() {
        return apiStrategy;
    }

    public void setApiStrategy(ApiStrategy apiStrategy) {
        this.apiStrategy = apiStrategy;
    }
}
