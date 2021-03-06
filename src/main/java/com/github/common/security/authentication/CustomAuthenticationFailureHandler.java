package com.github.common.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setContentType(SysConstant.CONTENT_TYPE_JSON);
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.ofMessage(
                ApiResponse.ResponseStatus.AUTHENTICATION_FAILED.getCode(), exception.getMessage())));
    }
}
