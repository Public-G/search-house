package com.github.common.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.constant.ApiReasonConstant;
import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import com.github.modules.sys.dto.SysMenuDTO;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.service.SysMenuService;
import com.github.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 认证成功处理器
 *
 * @author ZEALER
 * @date 2018-10-25
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    private SessionStrategy sessionStrategy;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 查询该用户的菜单
        SysUserEntity     sysUserEntity = (SysUserEntity) authentication.getPrincipal();
        List<SysMenuDTO>  userMenuList  = sysMenuService.getUserMenuList(sysUserEntity.getUserId());
        ServletWebRequest webRequest    = new ServletWebRequest(request, response);
        sessionStrategy.setAttribute(webRequest, SysConstant.SESSION_KEY_USER_MENU, userMenuList);

        // 更新登陆时间
        sysUserEntity.setLastLoginTime(new Date());
        sysUserService.update(sysUserEntity);

        response.setContentType(SysConstant.CONTENT_TYPE_JSON);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.ofMessage(ApiResponse.ResponseStatus.SUCCESS.getCode(),
                                      ApiReasonConstant.LOGIN_SUCCESS_MSG)));
    }

    public void setSessionStrategy(SessionStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }
}
