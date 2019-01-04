package com.github.modules.data.controller;

import com.github.common.annotation.SysLog;
import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.entity.SettingEntity;
import com.github.modules.data.entity.SpiderEntity;
import com.github.modules.data.service.SettingService;
import com.github.modules.data.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 爬虫参数设置
 *
 * @author ZEALER
 * @date 2018-11-23
 */
@RequestMapping("/data/setting")
@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private SpiderService spiderService;


    /**
     * 列表/添加 页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/data/setting/setting" + forwardType;
    }

    /**
     * 解析规则列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm){
        PageUtils pageBean = settingService.findPage(pageForm);

        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 唯一性校验
     */
    @GetMapping("/verify")
    @ResponseBody
    public ApiResponse verify(@RequestParam String settingName) {
        SettingEntity settingEntity = settingService.findByName(settingName);
        if (settingEntity == null) {
            return ApiResponse.ofSuccess();
        }

        return ApiResponse.ofFail("参数名已存在");
    }

    /**
     * 保存参数信息
     */
    @SysLog("保存爬虫参数")
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(SettingEntity settingEntity){
        ValidatorUtils.validateEntity(settingEntity);

        settingService.save(settingEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 参数信息
     */
    @GetMapping("/info/{settingId:[0-9]+}")
    public String info(@PathVariable Long settingId, Model model){
        model.addAttribute("setting", settingService.findById(settingId));

        return "admin/data/setting/settingInfo";
    }

    /**
     * 修改参数信息
     */
    @SysLog("修改爬虫参数")
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("setting") SettingEntity settingEntity){
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (settingEntity == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        ValidatorUtils.validateEntity(settingEntity);

        settingService.update(settingEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除参数信息
     */
    @SysLog("删除爬虫参数")
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam("selectIds") Long[] settingIds) {
        List<SpiderEntity> spiderEntities = spiderService.findBySettingIdIn(settingIds);
        if (null != spiderEntities && spiderEntities.size() > 0) {
            throw new SHException("项目中已使用，请先删除关联");
        }

        settingService.deleteBatch(settingIds);

        return ApiResponse.ofSuccess();
    }
    
    @ModelAttribute
    private void customModelAttribute(@RequestParam(value = "settingId", required = false) Long settingId, Model model) {
        if (settingId != null) {
            model.addAttribute("setting", settingService.findById(settingId));
        }
    }

}
