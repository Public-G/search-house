package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.ScheduleJobEntity;

/**
 * 定时任务
 *
 * @author ZEALER
 * @date 2018-11-08
 */
public interface ScheduleJobService {

    PageUtils findPage(PageForm pageForm);

    /**
     * 保存定时任务
     * @param scheduleJobEntity
     */
    void save(ScheduleJobEntity scheduleJobEntity);

    /**
     * 更新定时任务
     * @param scheduleJobEntity
     */
    void update(ScheduleJobEntity scheduleJobEntity);

    /**
     * 根据jobId查询定时任务
     * @param jobId
     * @return
     */
    ScheduleJobEntity findByJobId(Long jobId);
}
