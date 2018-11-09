package com.github.modules.data.controller;

import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.repository.ScheduleJobLogRepository;
import com.github.modules.data.service.ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author ZEALER
 * @date 2018-11-09
 */
@Controller
@RequestMapping("/data/scheduleLog")
public class ScheduleJobLogController {

    @Autowired
    private ScheduleJobLogService scheduleJobLogService;

    /**
     * 列表/添加页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/data/schedule/scheduleLog" + forwardType;
    }

    /**
     * 定时任务日志列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm){
        PageUtils pageBean = scheduleJobLogService.findPage(pageForm);

        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

}
