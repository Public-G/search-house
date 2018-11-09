package com.github.modules.data.controller;

import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.modules.data.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 房源管理
 *
 * @author ZEALER
 * @date 2018-11-01
 */
@RequestMapping("/data/house")
@Controller
public class HouseController {

    @Autowired
    private HouseService houseService;

    /**
     * 列表/添加/更新页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/data/house/house" + forwardType;
    }

    /**
     * 所有房源数据列表
     *
     * @param params 请求参数
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(@RequestParam Map<String, String> params) {
        PageUtils pageBean = houseService.findPage(params);
        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

}
