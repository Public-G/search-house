package com.github.common.constant;

/**
 * 返回数据中 code 对应的 standardMessage常量
 *
 * @author ZEALER
 * @date 2018-10-24
 */
public class ApiReasonConstant {

    public static final String SUCCESS_MSG = "操作成功";

    public static final String UNAUTHORIZED_MSG = "请先登录";

    public static final String BAD_REQUEST_MSG = "请求无效";

    public static final String FORBIDDEN_MSG = "没有权限，请联系管理员授权";

    public static final String NOT_FOUND_MSG = "路径不存在，请检查路径是否正确";

    public static final String INTERNAL_SERVER_ERROR_MSG = "服务器遇到了一个未曾预料的状况";

    public static final String NOT_VALID_PARAM_MSG = "无效的参数";

    public static final String AUTHENTICATION_FAILED_MSG = "校验失败, 请检查请求参数";

}
