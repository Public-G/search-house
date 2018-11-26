package com.github.modules.data.controller;

import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.entity.ScheduleJobEntity;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.service.RuleService;
import com.github.modules.data.service.SupportAreaService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 解析规则
 *
 * @author ZEALER
 * @date 2018-11-11
 */
@RequestMapping("/data/rule")
@Controller
public class RuleController {

    @Autowired
    private RuleService ruleService;

    /**
     * 列表/添加 页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/data/rule/rule" + forwardType;
    }

    /**
     * 解析规则列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm){
        PageUtils pageBean = ruleService.findPage(pageForm);

        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 唯一性校验
     */
    @GetMapping("/verify")
    @ResponseBody
    public ApiResponse verify(@RequestParam String ruleName) {
        RuleEntity ruleEntity = ruleService.findByName(ruleName);
        if (ruleEntity == null) {
            return ApiResponse.ofSuccess();
        }

        return ApiResponse.ofFail("规则名已存在");
    }

    /**
     * 保存解析规则
     */
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(RuleEntity ruleEntity){
        ValidatorUtils.validateEntity(ruleEntity, AddGroup.class);

        ruleService.save(ruleEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 解析规则信息
     */
    @GetMapping("/info/{ruleId:[1-9]+}")
    public String info(@PathVariable Long ruleId, Model model){
        model.addAttribute("rule", ruleService.findById(ruleId));

        return "admin/data/rule/ruleInfo";
    }

    /**
     * 修改解析规则
     */
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("rule") RuleEntity ruleEntity){
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (ruleEntity == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        ValidatorUtils.validateEntity(ruleEntity, UpdateGroup.class);

        ruleService.update(ruleEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除解析规则
     */
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam("selectIds") Long[] ruleIds) {
        ruleService.deleteBatch(ruleIds);

        return ApiResponse.ofSuccess();
    }

    @ModelAttribute
    private void customModelAttribute(@RequestParam(value = "ruleId", required = false) Long ruleId, Model model) {
        if (ruleId != null) {
            model.addAttribute("rule", ruleService.findById(ruleId));
        }
    }
}
