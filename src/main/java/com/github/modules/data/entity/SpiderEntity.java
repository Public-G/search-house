package com.github.modules.data.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 爬虫项目管理
 *
 * @author ZEALER
 * @date 2018-11-12
 */
@Table(name = "tb_spider")
@Entity
public class SpiderEntity implements Serializable {
    private static final long serialVersionUID = 90933415L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spider_id")
    private Long spiderId;

    /**
     * 项目名称
     */
    @Column(name = "spider_name")
    @NotBlank(message="项目名称不能为空")
    private String spiderName;

    /**
     * 起始链接
     */
    @Column(name = "start_urls")
    @NotBlank(message="起始链接不能为空")
    private String startUrls;

    /**
     * 城市名
     */
    @NotBlank(message = "城市不能为空")
    private String city;

    /**
     * 规则ID，逻辑外键
     */
    @Column(name = "rule_id")
    @NotNull(message = "解析规则不能为空")
    private Long ruleId;

    @Transient
    private String ruleName;

    /**
     * 参数ID，逻辑外键
     */
    @Column(name = "setting_id")
    private Long settingId;

    @Transient
    private String settingName;

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

    public SpiderEntity() {
    }

    public SpiderEntity(String spiderName, String startUrls, String city, Long ruleId, String ruleName, Long settingId, String settingName, String remark, Date createTime, Date updateTime) {
        this.spiderName = spiderName;
        this.startUrls = startUrls;
        this.city = city;
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.settingId = settingId;
        this.settingName = settingName;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getSpiderId() {
        return spiderId;
    }

    public void setSpiderId(Long spiderId) {
        this.spiderId = spiderId;
    }

    public String getSpiderName() {
        return spiderName;
    }

    public void setSpiderName(String spiderName) {
        this.spiderName = spiderName;
    }

    public String getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(String startUrls) {
        this.startUrls = startUrls;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
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
