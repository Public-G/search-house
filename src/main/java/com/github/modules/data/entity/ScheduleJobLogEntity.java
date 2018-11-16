package com.github.modules.data.entity;


import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务日志
 *
 * @author ZEALER
 * @date 2018-11-08
 */
@Table(name = "schedule_job_log")
@Entity
public class ScheduleJobLogEntity implements Serializable {
    private static final long serialVersionUID = 19986421L;

    /**
     * 日志id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    /**
     * 任务id
     */
    @Column(name = "job_id")
    private Long jobId;

    /**
     * spring bean名称
     */
    @Column(name = "bean_name")
    private String beanName;

    /**
     * 方法名
     */
    @Column(name = "method_name")
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * 任务状态    0：成功    1：失败
     */
    private int status;

    /**
     * 失败信息
     */
    private String error;

    /**
     * 耗时(单位：毫秒)
     */
    private Integer times;

    /**
     * 执行时间
     */
    @Column(name = "create_time")
    private Date createTime;

    public ScheduleJobLogEntity() {
    }

    public ScheduleJobLogEntity(Long logId, Long jobId, String beanName, String methodName, String params, int status, String error, Integer times, Date createTime) {
        this.logId = logId;
        this.jobId = jobId;
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
        this.status = status;
        this.error = error;
        this.times = times;
        this.createTime = createTime;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
