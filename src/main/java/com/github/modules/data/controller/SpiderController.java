package com.github.modules.data.controller;

import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.entity.SettingEntity;
import com.github.modules.data.entity.SpiderEntity;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.service.RuleService;
import com.github.modules.data.service.SettingService;
import com.github.modules.data.service.SpiderService;
import com.github.modules.data.service.SupportAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 爬虫项目管理
 *
 * @author ZEALER
 * @date 2018-11-12
 */
@RequestMapping("/data/spider")
@Controller
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private SettingService settingService;

    /**
     * 列表/添加 页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType, Model model) {
        if ("Add".equals(forwardType)) {
            reference(model);
        }

        return "admin/data/spider/spider" + forwardType;
    }

    /**
     * 爬虫项目列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm) {
        PageUtils pageBean = spiderService.findPage(pageForm);

        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 保存爬虫项目
     */
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(SpiderEntity spiderEntity) {
        ValidatorUtils.validateEntity(spiderEntity);

        spiderService.save(spiderEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 唯一性校验
     */
    @GetMapping("/verify")
    @ResponseBody
    public ApiResponse verify(@RequestParam String spiderName) {
        SpiderEntity spiderEntity = spiderService.findByName(spiderName);
        if (spiderEntity == null) {
            return ApiResponse.ofSuccess();
        }

        return ApiResponse.ofFail("项目名已存在");
    }

    /**
     * 爬虫项目信息
     */
    @GetMapping("/info/{spiderId:[1-9]+}")
    public String info(@PathVariable Long spiderId, Model model) {
        SpiderEntity spider = spiderService.findById(spiderId);
        model.addAttribute("spider", spider);

        reference(model);

        return "admin/data/spider/spiderInfo";
    }

    /**
     * 修改爬虫项目
     */
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("spider") SpiderEntity spiderEntity) {
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (spiderEntity == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        ValidatorUtils.validateEntity(spiderEntity);

        spiderService.update(spiderEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除爬虫项目
     */
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam("selectIds") Long[] spiderIds) {
        spiderService.deleteBatch(spiderIds);

        return ApiResponse.ofSuccess();
    }

    private void reference(Model model) {
        List<RuleEntity>    allRule    = ruleService.findAll();
        List<SettingEntity> allSetting = settingService.findAll();
        model.addAttribute("rule", allRule);
        model.addAttribute("setting", allSetting);
    }

    @ModelAttribute
    private void customModelAttribute(@RequestParam(value = "spiderId", required = false) Long spiderId, Model model) {
        if (spiderId != null) {
            model.addAttribute("spider", spiderService.findById(spiderId));
        }
    }

}
