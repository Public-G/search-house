package com.github.common.exception;

import com.github.common.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Web错误全局配置，替换 BasicErrorController
 *
 * @author ZEALER
 * @date 2018-10-20
 */
@Controller
public class SHErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;
    private static final String ERROR_PATH = "/error";

    @Autowired
    public SHErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * Web页面错误处理
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String errorPageHandler(HttpServletResponse response) {
        int status = response.getStatus();
        switch (status) {
            case 403:
                return "error/403";
            case 404:
                return "error/404";
            case 500:
                return "error/500";
        }
        return "error/error";
    }

    /**
     * 除Web页面外的错误处理，比如Json/XML等
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public ApiResponse errorApiHandler(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(requestAttributes, false);

        int status = getStatus(request);
        String message = ApiResponse.ResponseStatus.ofApiReasonPhrase(status);

        return ApiResponse.ofMessage(status, message.equals("") ?
                String.valueOf(attributes.getOrDefault("message", "error")) : message );
    }

    private int getStatus(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (status != null) {
            return status;
        }

        return ApiResponse.ResponseStatus.INTERNAL_SERVER_ERROR.getCode();
    }

}
