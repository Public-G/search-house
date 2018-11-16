package com.github.modules.data.entity;

import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务
 *
 * @author ZEALER
 * @date 2018-11-08
 */
@Table(name = "schedule_job")
@Entity
public class ScheduleJobEntity implements Serializable {
    private static final long serialVersionUID = 554313882152L;

    /**
     * 任务id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    @NotNull(message = "该记录不存在，请检查是否被删除",  groups = {UpdateGroup.class})
    private Long jobId;

    /**
     * spring bean名称
     */
    @Column(name = "bean_name")
    @NotBlank(message="bean名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String beanName;

    /**
     * 方法名
     */
    @Column(name = "method_name")
    @NotBlank(message="方法名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */
    @Column(name = "cron_expression")
    @NotBlank(message="cron表达式不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String cronExpression;

    /**
     * 任务状态
     */
    private int status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;


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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
