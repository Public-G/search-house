package com.github.common.utils;

import com.github.common.constant.ApiReasonConstant;
import com.github.common.constant.SysConstant;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;


/**
 * 返回数据格式封装
 *
 * @author ZEALER
 * @date 2018-10-20
 */
public class ApiResponse extends HashMap<String, Object> {

    public ApiResponse() {
        put("code", ResponseStatus.SUCCESS.getCode());
        put("msg", ResponseStatus.SUCCESS.getStandardMessage());
    }

    public static ApiResponse ofSuccess() {
        return new ApiResponse();
    }

    public ApiResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static ApiResponse ofFail(String ... message) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.put("code", ResponseStatus.FAIL.getCode());
        String msg = message[0];
        if (StringUtils.isBlank(msg)) {
            apiResponse.put("msg", ResponseStatus.FAIL.getStandardMessage());
        } else {
            apiResponse.put("msg", msg);
        }

        return apiResponse;
    }

    public static ApiResponse ofError() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.put("code", ResponseStatus.INTERNAL_SERVER_ERROR.getCode());
        apiResponse.put("msg", ResponseStatus.INTERNAL_SERVER_ERROR.getStandardMessage());

        return apiResponse;
    }

    public static ApiResponse ofMessage(int code, String message) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.put("code", code);
        apiResponse.put("msg", message);

        return apiResponse;
    }

    public static ApiResponse ofStatus(ResponseStatus status) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.put("code", status.getCode());
        apiResponse.put("msg", status.getStandardMessage());

        return apiResponse;
    }


    public enum ResponseStatus{
        SUCCESS(SysConstant.SUCCESS_CODE, ApiReasonConstant.SUCCESS_MSG),
        FAIL(SysConstant.FAIL_CODE, ApiReasonConstant.FAIL_MSG),
        UNAUTHORIZED(401, ApiReasonConstant.UNAUTHORIZED_MSG),
        BAD_REQUEST(400, ApiReasonConstant.BAD_REQUEST_MSG),
        FORBIDDEN(403, ApiReasonConstant.FORBIDDEN_MSG),
        NOT_FOUND(404, ApiReasonConstant.NOT_FOUND_MSG),
        INTERNAL_SERVER_ERROR(500, ApiReasonConstant.INTERNAL_SERVER_ERROR_MSG),
        NOT_VALID_PARAM(40001, ApiReasonConstant.NOT_VALID_PARAM_MSG),
        AUTHENTICATION_FAILED(40003, ApiReasonConstant.AUTHENTICATION_FAILED_MSG);

        private int code;
        private String standardMessage;

        ResponseStatus(int code, String standardMessage) {
            this.code = code;
            this.standardMessage = standardMessage;
        }

        public static String ofApiReason(Integer status) {
            for (ResponseStatus responseStatus : ResponseStatus.values()) {
                if (responseStatus.getCode() == status) {
                    return responseStatus.getStandardMessage();
                }
            }
            return "";
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getStandardMessage() {
            return standardMessage;
        }

        public void setStandardMessage(String standardMessage) {
            this.standardMessage = standardMessage;
        }
    }

}
