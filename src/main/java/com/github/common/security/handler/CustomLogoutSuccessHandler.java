package com.github.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.constant.ApiReasonConstant;
import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出成功处理器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Component("logoutSuccessHandler")
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.setContentType(SysConstant.CONTENT_TYPE_JSON);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.ofMessage(ApiResponse.ResponseStatus.SUCCESS.getCode(), ApiReasonConstant.LOGOUT_SUCCESS_MSG)));
    }
}
