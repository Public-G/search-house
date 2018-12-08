package com.github.modules.data.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * 爬虫参数设置
 *
 * @author ZEALER
 * @date 2018-11-22
 */
@Table(name = "tb_setting")
@Entity
public class SettingEntity implements Serializable {
    private static final long serialVersionUID = 9987122L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long settingId;

    @Column(name = "setting_name")
    @NotBlank(message = "参数名称不能为空")
    private String settingName;

    /**
     * 开启cookies, 0：开启，1：禁用
     */
    @Column(name = "cookies_enabled")
    private Integer cookiesEnabled;

    /**
     * 并发请求数，最大32，默认16
     */
    @Column(name = "concurrent_requests")
    @Min(message = "并发请求数不能小于0", value = 0)
    @Max(message = "并发请求数不能大于32",value = 32)
    private Integer concurrentRequests;

    /**
     * 针对每个域名限制n个并发，最大为16个
     */
    @Column(name = "concurrent_requests_per_domain")
    @Min(message = "域名限制最小0个并发", value = 0)
    @Max(message = "域名限制最大16个并发",value = 32)
    private Integer concurrentRequestsPerDomain;

    /**
     * 智能的限速请求，0：开启，1禁用
     */
    @Column(name = "autothrottle_enabled")
    private Integer autothrottleEnabled;

    /**
     * 初始下载延迟
     */
    @Column(name = "autothrottle_start_delay")
    @Min(message = "初始下载延迟最小值不能小于0", value = 0)
    private Integer autothrottleStartDelay;

    /**
     * 最小下载延迟秒数，限制频繁访问
     */
    @Column(name = "download_delay")
    @Min(message = "下载延迟秒数最小值不能小于0", value = 0)
    private Integer downloadDelay;

    /**
     * 最大下载延迟秒数
     */
    @Column(name = "autothrottle_max_delay")
    @Min(message = "最大下载延迟秒数不能小于0", value = 0)
    private Integer autothrottleMaxDelay;

    /**
     * Scrapy请求的平均数量应该并行发送每个远程服务器
     */
    @Column(name = "autothrottle_target_concurrency")
    @Min(message = "并行的请求的平均数量不能小于0", value = 0)
    private Float autothrottleTargetConcurrency;

    /**
     * 爬虫允许的最大深度，0表示无限制(爬取深度不是必须的，空字符串转换Integer为null，而无法转换为int)
     */
    @Column(name = "depth_limit")
    @Min(message = "爬虫允许的最大深度不能小于0", value = 0)
    private Integer depthLimit;

    /**
     * 0：深度优先LIFO(默认)，1：广度优先FIFO
     */
    @Column(name = "depth_priority")
    private Integer depthPriority;

    /**
     * 缓存 0：开启，1：关闭
     */
    @Column(name = "httpcache_enabled")
    private Integer httpcacheEnabled;

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

    public SettingEntity() {
    }

    public SettingEntity(String settingName, Integer cookiesEnabled, Integer concurrentRequests, Integer concurrentRequestsPerDomain, Integer autothrottleEnabled, Integer autothrottleStartDelay, Integer downloadDelay, Integer autothrottleMaxDelay, Float autothrottleTargetConcurrency, Integer depthLimit, Integer depthPriority, Integer httpcacheEnabled, Date createTime, Date updateTime) {
        this.settingName = settingName;
        this.cookiesEnabled = cookiesEnabled;
        this.concurrentRequests = concurrentRequests;
        this.concurrentRequestsPerDomain = concurrentRequestsPerDomain;
        this.autothrottleEnabled = autothrottleEnabled;
        this.autothrottleStartDelay = autothrottleStartDelay;
        this.downloadDelay = downloadDelay;
        this.autothrottleMaxDelay = autothrottleMaxDelay;
        this.autothrottleTargetConcurrency = autothrottleTargetConcurrency;
        this.depthLimit = depthLimit;
        this.depthPriority = depthPriority;
        this.httpcacheEnabled = httpcacheEnabled;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public Integer getCookiesEnabled() {
        return cookiesEnabled;
    }

    public void setCookiesEnabled(Integer cookiesEnabled) {
        this.cookiesEnabled = cookiesEnabled;
    }

    public Integer getConcurrentRequests() {
        return concurrentRequests;
    }

    public void setConcurrentRequests(Integer concurrentRequests) {
        this.concurrentRequests = concurrentRequests;
    }

    public Integer getConcurrentRequestsPerDomain() {
        return concurrentRequestsPerDomain;
    }

    public void setConcurrentRequestsPerDomain(Integer concurrentRequestsPerDomain) {
        this.concurrentRequestsPerDomain = concurrentRequestsPerDomain;
    }

    public Integer getAutothrottleEnabled() {
        return autothrottleEnabled;
    }

    public void setAutothrottleEnabled(Integer autothrottleEnabled) {
        this.autothrottleEnabled = autothrottleEnabled;
    }

    public Integer getAutothrottleStartDelay() {
        return autothrottleStartDelay;
    }

    public void setAutothrottleStartDelay(Integer autothrottleStartDelay) {
        this.autothrottleStartDelay = autothrottleStartDelay;
    }

    public Integer getDownloadDelay() {
        return downloadDelay;
    }

    public void setDownloadDelay(Integer downloadDelay) {
        this.downloadDelay = downloadDelay;
    }

    public Integer getAutothrottleMaxDelay() {
        return autothrottleMaxDelay;
    }

    public void setAutothrottleMaxDelay(Integer autothrottleMaxDelay) {
        this.autothrottleMaxDelay = autothrottleMaxDelay;
    }

    public Float getAutothrottleTargetConcurrency() {
        return autothrottleTargetConcurrency;
    }

    public void setAutothrottleTargetConcurrency(Float autothrottleTargetConcurrency) {
        this.autothrottleTargetConcurrency = autothrottleTargetConcurrency;
    }

    public Integer getDepthLimit() {
        return depthLimit;
    }

    public void setDepthLimit(Integer depthLimit) {
        this.depthLimit = depthLimit;
    }

    public Integer getDepthPriority() {
        return depthPriority;
    }

    public void setDepthPriority(Integer depthPriority) {
        this.depthPriority = depthPriority;
    }

    public Integer getHttpcacheEnabled() {
        return httpcacheEnabled;
    }

    public void setHttpcacheEnabled(Integer httpcacheEnabled) {
        this.httpcacheEnabled = httpcacheEnabled;
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
