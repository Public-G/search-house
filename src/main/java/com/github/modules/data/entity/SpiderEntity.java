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
     * 爬取深度(爬取深度不是必须的，空字符串转换Integer为null，而无法转换为int)
     */
    @Column(name = "depth_limit")
    @Min(value = 1)
    @Max(value = Integer.MAX_VALUE)
    private Integer depthLimit;

    /**
     * 城市ID，逻辑外键
     */
    @Column(name = "city_id")
    @NotNull(message = "城市不能为空")
    private Long cityId;

    /**
     * 规则ID，逻辑外键
     */
    @Column(name = "rule_id")
    @NotNull(message = "解析规则不能为空")
    private Long ruleId;

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

    public SpiderEntity(Long spiderId, String spiderName, String startUrls, Integer depthLimit, Long cityId, Long ruleId, String remark, Date createTime, Date updateTime) {
        this.spiderId = spiderId;
        this.spiderName = spiderName;
        this.startUrls = startUrls;
        this.depthLimit = depthLimit;
        this.cityId = cityId;
        this.ruleId = ruleId;
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

    public Integer getDepthLimit() {
        return depthLimit;
    }

    public void setDepthLimit(Integer depthLimit) {
        this.depthLimit = depthLimit;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
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
