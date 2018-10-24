package com.github.modules.sys.controller;

import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.ApiStrategy;
import com.github.common.validator.ValidatorUtils;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId:\\d+}")
    public ApiResponse info(@PathVariable("userId") Long userId){

        SysUserEntity user = sysUserService.selectUserById(userId);
        return ApiResponse.ofSuccess().put("user", user);
    }

}
