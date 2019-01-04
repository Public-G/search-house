package com.github.modules.sys.controller;

import com.github.modules.sys.entity.SysUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Controller公共组件
 *
 * @author ZEALER
 * @date 2018-10-22
 */
public class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected SysUserEntity getUser() {
        return (SysUserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }
}
