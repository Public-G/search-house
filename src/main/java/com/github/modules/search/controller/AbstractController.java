package com.github.modules.search.controller;


import com.github.common.constant.SysConstant;
import com.github.modules.data.service.SpiderService;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Controller公共组件
 *
 * @author ZEALER
 * @date 2018-12-06
 */
public abstract class AbstractController {

//    protected String checkCity(SpiderService spiderService, String city, Model model, String toPage) {
//        List<String> allCity = spiderService.findCity();
//
//        Boolean isContains = allCity.contains(city);
//        if (!isContains) {
//            return "error/404";
//        }
//
//        model.addAttribute("city", city);
//        return toPage;
//    }

}
