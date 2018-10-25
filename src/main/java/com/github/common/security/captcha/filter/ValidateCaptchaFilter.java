package com.github.common.security.captcha.filter;

import com.github.common.security.captcha.CaptchaType;
import com.github.common.security.captcha.support.ValidateCaptchaProcessorHolder;
import com.github.common.security.constant.SecurityConstant;
import com.github.common.security.exception.ValidateCaptchaException;
import com.github.common.security.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 校验验证码过滤器
 *
 * 在Servlet3.0中，如果一个请求是DispatcherType.ASYNC类型的，
 * 那么在一个单一请求的过程中，filter能够被多个线程调用，
 * 也就是意味着一个filter可能在一次请求中被多次执行
 * {@link OncePerRequestFilter} 确保在一次请求只通过一次filter
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Component
public class ValidateCaptchaFilter extends OncePerRequestFilter implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 验证码校验失败处理器(默认的)
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * SpringSecurity全局Properties配置
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 系统中的校验码处理器
     */
    @Autowired
    private ValidateCaptchaProcessorHolder validateCaptchaProcessorHolder;

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    @Autowired
    private AntPathMatcher pathMatcher;

    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String, CaptchaType> urlMap;

    /**
     * 初始化要拦截的url配置信息
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap = new HashMap<>();

        urlMap.put(SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_FORM, CaptchaType.IMAGE);
        addUrlToMap(securityProperties.getCaptcha().getImage().getUrl(), CaptchaType.IMAGE);

        urlMap.put(SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, CaptchaType.SMS);
        addUrlToMap(securityProperties.getCaptcha().getSms().getUrl(), CaptchaType.SMS);
    }

    /**
     * 将手动配置的验证码url添加到Map中
     *
     * @param urlString    手动配置的 url
     * @param type         验证码类型
     */
    protected void addUrlToMap(String urlString, CaptchaType type) {
        if (StringUtils.isNotBlank(urlString)) {
            // 不忽略任何空白项, " "不会被忽略算一个元素, 二参数为null默认为空格分隔
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, type);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CaptchaType captchaType = getCaptchaType(request);

        if (captchaType != null) {
            logger.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + captchaType);
            try {
                // 校验验证码
                validateCaptchaProcessorHolder.findCaptchaProcessor(captchaType)
                        .validateCaptcha(new ServletWebRequest(request, response));
                logger.info("验证码校验通过");
            } catch (ValidateCaptchaException e) {
                // 调用失败处理器
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     */
    private CaptchaType getCaptchaType(HttpServletRequest request) {
        CaptchaType captchaType = null;
        //如果不是get请求
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                //匹配上的URI，从Map中拿出来
                if (pathMatcher.match(url, request.getRequestURI())) {
                    captchaType = urlMap.get(url);
                }
            }
        }
        return captchaType;
    }
}
