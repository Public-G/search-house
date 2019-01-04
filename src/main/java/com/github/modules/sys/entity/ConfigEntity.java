package com.github.modules.sys.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统静态资源配置
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Table(name = "tb_config")
@Entity
public class ConfigEntity implements Serializable {
    private static final long serialVersionUID = 97671113534534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "config_key")
    private String configKey;

    @Column(name = "config_value")
    private String configValue;

    @Column(name = "config_comment")
    private String configComment;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTtime;

    public ConfigEntity() {
    }

    public ConfigEntity(String configKey, String configValue, String configComment, Date createTime, Date updateTtime) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.configComment = configComment;
        this.createTime = createTime;
        this.updateTtime = updateTtime;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigComment() {
        return configComment;
    }

    public void setConfigComment(String configComment) {
        this.configComment = configComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTtime() {
        return updateTtime;
    }

    public void setUpdateTtime(Date updateTtime) {
        this.updateTtime = updateTtime;
    }
}
