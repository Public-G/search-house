package com.github.modules.sys.controller;

import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.search.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 普通用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Controller
@RequestMapping("/sys/front/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 列表页面跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/tbUser/user" + forwardType;
    }

    /**
     * 所有用户列表
     *
     * @param pageForm 请求参数
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm) {
        PageUtils pageBean = userService.findPage(pageForm);
        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }
}

