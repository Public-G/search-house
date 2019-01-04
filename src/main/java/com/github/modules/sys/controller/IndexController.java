package com.github.modules.sys.controller;

import com.github.modules.sys.entity.SysUserEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 后台首页
 *
 * @author ZEALER
 * @date 2018-10-25
 */
@Controller("sysIndexController")
public class IndexController {

    @GetMapping("/admin/index")
    public String index(@AuthenticationPrincipal UserDetails user) {
        if (user instanceof SysUserEntity) {
            return "admin/index";
        }
        return "admin/login";
    }

    @GetMapping("admin/main")
    public String main(@AuthenticationPrincipal UserDetails user) {
        if (user instanceof SysUserEntity) {
            return "admin/main";
        }
        return "admin/login";
    }
}
