package com.github.modules.data.controller;

import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.ScheduleJobEntity;
import com.github.modules.data.service.ScheduleJobService;
import com.github.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

/**
 * 定时任务
 *
 * @author ZEALER
 * @date 2018-11-09
 */
@Controller
@RequestMapping("/data/schedule")
public class ScheduleJobController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 列表/添加页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/data/schedule/schedule" + forwardType;
    }

    /**
     * 定时任务列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm){
        PageUtils pageBean = scheduleJobService.findPage(pageForm);

        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 保存定时任务
     */
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(ScheduleJobEntity scheduleJobEntity){
        ValidatorUtils.validateEntity(scheduleJobEntity, AddGroup.class);

        scheduleJobService.save(scheduleJobEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 定时任务信息
     */
    @GetMapping("/info/{jobId:[1-9]+}")
    public String info(@PathVariable Long jobId, Model model){
        model.addAttribute("schedule", scheduleJobService.findByJobId(jobId));

        return "admin/data/schedule/scheduleInfo";
    }

    /**
     * 修改定时任务
     */
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("schedule") ScheduleJobEntity scheduleJobEntity){
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (scheduleJobEntity.getJobId() == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        ValidatorUtils.validateEntity(scheduleJobEntity, UpdateGroup.class);

        scheduleJobService.update(scheduleJobEntity);

        return ApiResponse.ofSuccess();
    }


    @ModelAttribute
    public void customModelAttribute(@RequestParam(value = "jobId", required = false) Long jobId, Model model) {
        if (jobId != null) {
            ScheduleJobEntity scheduleJobEntity = scheduleJobService.findByJobId(jobId);
            model.addAttribute("schedule", scheduleJobEntity);
        }
    }
}
