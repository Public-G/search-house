package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.ScheduleJobLogEntity;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author ZEALER
 * @date 2018-11-08
 */
public interface ScheduleJobLogService {

    PageUtils findPage(PageForm pageForm);

    void save(ScheduleJobLogEntity scheduleJobLogEntity);
}
