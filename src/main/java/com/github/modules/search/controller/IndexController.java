package com.github.modules.search.controller;

import com.github.common.constant.SysConstant;
import com.github.modules.data.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 首页
 *
 * @author ZEALER
 * @date 2018-12-02
 */
@Controller
public class IndexController extends AbstractController {

    @Autowired
    private SpiderService spiderService;

    @GetMapping("/")
    public String index(@RequestParam(name = "city", required = false,
                                      defaultValue = SysConstant.DEFAULT_CITY) String city, Model model) {

        model.addAttribute("city", city);
        return "front/index";
    }

}
