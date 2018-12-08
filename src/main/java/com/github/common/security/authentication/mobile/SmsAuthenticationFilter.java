package com.github.common.security.authentication.mobile;

import com.github.common.security.constant.SecurityConstant;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 短信登录过滤器
 *
 * @see UsernamePasswordAuthenticationFilter 参考用户名密码登录过滤器
 * @author ZEALER
 * @date 2018-10-25
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String FORM_MOBILE_KEY = "mobile";

    private String mobileParameter = FORM_MOBILE_KEY;
    private boolean postOnly = true; // 只处理POST请求

    public SmsAuthenticationFilter() {
        // 传入要拦截的URI
        super(new AntPathRequestMatcher(SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 如果不是POST请求抛出异常
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String mobile = this.obtainUsername(request);
            if (mobile == null) {
                mobile = "";
            }

            mobile = mobile.trim();
            SmsAuthenticationToken authRequest = new SmsAuthenticationToken(mobile);

            // 把请求的信息设置到Token
            this.setDetails(request, authRequest);

            // 调用AuthenticationManager处理登录
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    /**
     * 获取手机号
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.mobileParameter);
    }

    /**
     * 把请求的信息设置到 SmsAuthenticationToken中(IP、SessionId等)
     */
    protected void setDetails(HttpServletRequest request, SmsAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.mobileParameter = usernameParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return this.mobileParameter;
    }

}
