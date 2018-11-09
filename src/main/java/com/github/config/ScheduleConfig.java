package com.github.config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 定时任务配置
 * 初始化Scheduler,启动任务调度程序
 *
 * @see SchedulerFactoryBean#afterPropertiesSet
 * @see SchedulerFactoryBean#start
 * @author ZEALER
 * @date 2018-11-07
 */
@Configuration
public class ScheduleConfig {

    @Bean
    public SchedulerFactoryBean SchedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);

        //quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", "SHScheduler"); // 调度标识名 集群中每一个实例都必须使用相同的名称
        prop.put("org.quartz.scheduler.instanceId", "AUTO"); // ID设置为自动获取 每一个必须不同

        //线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20"); // 线程数量 执行调度最大的线程数
        prop.put("org.quartz.threadPool.threadPriority", "5"); // 线程优先级

        //JobStore配置
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");

        //集群配置
        prop.put("org.quartz.jobStore.isClustered", "true"); // 加入集群
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000"); // 调度实例失效的检查时间间隔
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");

        prop.put("org.quartz.jobStore.misfireThreshold", "12000"); // 容许的最大作业延长时间
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE LOCK_NAME = ?");

        factory.setQuartzProperties(prop);

        factory.setSchedulerName("SHScheduler");
        //延时启动，这个很重要，必须要有足够长的时间让你的应用先启动完成后再让 Scheduler启动
        factory.setStartupDelay(30);
        //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        //设置自动启动，默认为true
        factory.setAutoStartup(true);

        return factory;
    }
}
