package com.github.modules.data.utils;


import com.github.common.constant.SysConstant;
import com.github.common.exception.SHException;
import com.github.modules.data.entity.ScheduleJobEntity;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author ZEALER
 * @date 2018-11-08
 */
public class ScheduleUtils {

    private final static String JOB_NAME = "JOB_";

    private final static String TRIGGER_KEY = "TRIGGER_";

    /**
     * 获取触发器 key
     */
    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(TRIGGER_KEY + jobId);
    }

    /**
     * 获取job Key
     */
    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new SHException("获取定时任务CronTrigger出现异常，请检查qrtz开头的表是否有脏数据", e);
        }
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJobEntity) {
        try {
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(scheduleJobEntity.getJobId()))
                    .build();

            //表达式调度构建器，忽略掉调度暂停过程中没有执行的调度(在进行quartz任务的暂停恢复时，quartz的任务会把之前因为暂停而错过的任务都会补充调度)
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJobEntity.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJobEntity.getJobId()))
                    .withSchedule(cronScheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(SysConstant.JOB_PARAM_KEY, scheduleJobEntity);

            // 安排执行任务job
            scheduler.scheduleJob(jobDetail, trigger);

            //暂停任务
            if (scheduleJobEntity.getStatus() == SysConstant.ScheduleStatus.PAUSE.getValue()) {
                pauseJob(scheduler, scheduleJobEntity.getJobId());
            }

        } catch (SchedulerException se) {
            throw new SHException("创建定时任务失败", se);

        } catch (Exception e) {
            throw new SHException("创建定时任务失败，请检查定时任务参数", e);
        }

    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJobEntity) {
        try {
            TriggerKey triggerKey = getTriggerKey(scheduleJobEntity.getJobId());

            //表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJobEntity.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = getCronTrigger(scheduler, scheduleJobEntity.getJobId());

            //按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

            //参数
            trigger.getJobDataMap().put(SysConstant.JOB_PARAM_KEY, scheduleJobEntity);

            scheduler.rescheduleJob(triggerKey, trigger);

            //暂停任务
            if (scheduleJobEntity.getStatus() == SysConstant.ScheduleStatus.PAUSE.getValue()) {
                pauseJob(scheduler, scheduleJobEntity.getJobId());
            }

        } catch (SchedulerException se) {
            throw new SHException("更新定时任务失败", se);

        } catch (Exception e) {
            throw new SHException("更新定时任务失败，请检查定时任务参数", e);
        }
    }

    /**
     * 立即执行任务
     * 马上执行一次该任务，只执行一次
     */
    public static void run(Scheduler scheduler, ScheduleJobEntity scheduleJobEntity) {
        try {
            //参数
            JobDataMap dataMap = new JobDataMap();

            dataMap.put(SysConstant.JOB_PARAM_KEY, scheduleJobEntity);

            scheduler.triggerJob(getJobKey(scheduleJobEntity.getJobId()), dataMap);
        } catch (SchedulerException e) {
            throw new SHException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     * 不是暂停正在执行的任务，而是以后不再执行这个定时任务了
     * 正在执行的任务，还是照常执行完
     */
    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new SHException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     * 针对pauseJob来的，如果任务暂停了，以后都不会再执行，
     * 要想再执行，则需要调用resumeJob，使定时任务恢复执行
     */
    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new SHException("恢复定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new SHException("删除定时任务失败", e);
        }
    }

}
