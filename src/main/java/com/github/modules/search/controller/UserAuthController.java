package com.github.modules.search.controller;

import com.github.common.constant.SysConstant;
import com.github.modules.data.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 普通用户认证
 *
 * @author ZEALER
 * @date 2018-12-06
 */
@RequestMapping("/user/auth")
@Controller
public class UserAuthController extends AbstractController {

    @Autowired
    private SpiderService spiderService;

    @RequestMapping("/register")
    public String register(@RequestParam(name = "city", required = false,
            defaultValue = SysConstant.DEFAULT_CITY) String city, Model model) {
        model.addAttribute("city",  city);
        return "front/register";
    }
}
