package com.github.modules.data.controller;

import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.service.SupportAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 支持区域
 *
 * @author ZEALER
 * @date 2018-10-31
 */
@Controller
@RequestMapping("/data/area")
public class SupportAreaController {

    @Autowired
    private SupportAreaService supportAreaService;

    /**
     * 列表/添加/更新页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/data/area/area" + forwardType;
    }

    /**
     * 所有区域列表
     *
     * @param params 请求参数
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(@RequestParam Map<String, String> params) {
        PageUtils pageBean = supportAreaService.findPage(params);
        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 保存区域
     */
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(SupportAreaEntity supportAreaEntity) {
//        ValidatorUtils.validateEntity(userEntity, AddGroup.class);

//        sysUserService.userSaveOrUpdate(userEntity);

        return ApiResponse.ofSuccess();
    }

    @GetMapping("/info/{id:[1-9]+}")
    public String info(@PathVariable Long id, Model model){
//        model.addAttribute("user", sysUserService.findByUserId(userId));
        return "admin/data/area/areaInfo";
    }




}
