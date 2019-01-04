package com.github.common.security.authentication.mobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.constant.ApiReasonConstant;
import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SmsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
//        System.out.println(objectMapper.writeValueAsString(authentication));
        response.setContentType(SysConstant.CONTENT_TYPE_JSON);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.ofMessage(ApiResponse.ResponseStatus.SUCCESS.getCode(),
                        ApiReasonConstant.LOGIN_SUCCESS_MSG)));
    }
}
