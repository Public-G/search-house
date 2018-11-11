package com.github.modules.data.utils;

import com.github.common.constant.SysConstant;
import com.github.common.utils.SpringContextUtils;
import com.github.modules.data.entity.ScheduleJobEntity;
import com.github.modules.data.entity.ScheduleJobLogEntity;
import com.github.modules.data.service.ScheduleJobLogService;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 定时任务
 *
 * 注意：该方式不能依赖注入spring bean组件
 * 必须在JobDetail或Trigger的属性JobDataMap中配置要注入的bean组件
 *
 * @author ZEALER
 * @date 2018-11-08
 */
public class ScheduleJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 单线程化的线程池，用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     */
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) {
        // 取JobDetail或Trigger中设置的map中的值
        ScheduleJobEntity scheduleJobEntity = (ScheduleJobEntity)context.getMergedJobDataMap()
                .get(SysConstant.JOB_PARAM_KEY);

        //获取spring bean
        ScheduleJobLogService scheduleJobLogService = SpringContextUtils.getBean("scheduleJobLogService", ScheduleJobLogService.class);

        //数据库保存执行记录
        ScheduleJobLogEntity log = new ScheduleJobLogEntity();
        log.setJobId(scheduleJobEntity.getJobId());
        log.setBeanName(scheduleJobEntity.getBeanName());
        log.setMethodName(scheduleJobEntity.getMethodName());
        log.setParams(scheduleJobEntity.getParams());
        log.setCreateTime(new Date());

        //任务开始时间
        long startTime = System.currentTimeMillis();

        try {
            logger.info("任务准备执行，任务ID：" + scheduleJobEntity.getJobId());

            //执行任务
            ScheduleRunnable task = new ScheduleRunnable(scheduleJobEntity.getBeanName(),
                    scheduleJobEntity.getMethodName(), scheduleJobEntity.getParams());
            Future<?> future = executor.submit(task);

            // 阻塞等待线程返回结果
            future.get();

            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes((int)times);
            //任务状态    0：成功    1：失败
            log.setStatus(0);

            logger.info("任务执行完毕，任务ID：" + scheduleJobEntity.getJobId() + "  总共耗时：" + times + "毫秒");

        } catch (Exception e) {
            logger.error("任务执行失败，任务ID：" + scheduleJobEntity.getJobId(), e);

            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes((int)times);

            //任务状态    0：成功    1：失败
            log.setStatus(1);
            log.setError(StringUtils.substring(e.toString(), 0, 2000));
        } finally {
            scheduleJobLogService.save(log);
        }
    }

//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//
//    }
}
