package com.github.modules.sys.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Table(name = "sys_log")
@Entity
public class SysLogEntity implements Serializable {
    private static final long serialVersionUID = 83229113534534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    private String username;

    private String operation;

    private String method;

    private String params;

    private Long time;

    private String ip;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public SysLogEntity() {
    }

    public SysLogEntity(Long logId, String username, String operation, String method, String params, Long time, String ip, Date createTime) {
        this.logId = logId;
        this.username = username;
        this.operation = operation;
        this.method = method;
        this.params = params;
        this.time = time;
        this.ip = ip;
        this.createTime = createTime;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
