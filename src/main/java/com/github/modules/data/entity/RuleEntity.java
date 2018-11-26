package com.github.modules.data.entity;

import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 解析规则
 *
 * @author ZEALER
 * @date 2018-11-11
 */
@Table(name = "tb_rule")
@Entity
public class RuleEntity implements Serializable {
    private static final long serialVersionUID = 613882152L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    /**
     * 规则名称
     */
    @Column(name = "rule_name")
    @NotBlank(message="规则名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String ruleName;

    /**
     * 允许爬取的域名
     */
    @Column(name = "allowed_domains")
    @NotBlank(message="允许爬取的域名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String allowedDomains;

    /**
     * 详情页循环入口
     */
    @Column(name = "loop_start")
    private String loopStart;

    /**
     * 下一页
     */
    @Column(name = "next_url")
    private String nextUrl;

    /**
     * 详情页
     */
    @Column(name = "detail_url")
    @NotBlank(message="详情页不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String detailUrl;

    /**
     * 区域
     */
    private String region;

    /**
     * 标题
     */
    @NotBlank(message="标题不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String title;

    /**
     * 小区
     */
    private String community;

    /**
     * 详细地址
     */
    @NotBlank(message="详细地址不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String address;

    /**
     * 价格
     */
    @NotBlank(message="价格不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String price;

    /**
     * 面积
     */
    @NotBlank(message="面积不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String square;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面图
     */
    @Column(name = "img_href")
    private String imgHref;

    /**
     * 户型
     */
    @Column(name = "house_type")
    private String houseType;

    /**
     * 出租方式
     */
    @Column(name = "rent_way")
    private String rentWay;

    /**
     * 来源网站
     */
    @Column(name = "website_name")
    @NotBlank(message="来源网站不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String websiteName;

    /**
     * 类型 0：个人  1：中介  2：使用分类器
     */
    @NotNull(message = "属性不能为空",  groups = {AddGroup.class, UpdateGroup.class})
    private int attribute;

    /**
     * 发布时间
     */
    @Column(name = "release_time")
    private String releaseTime;

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

    public RuleEntity() {
    }

    public RuleEntity(String ruleName, String allowedDomains, String loopStart, String nextUrl, String detailUrl, String region, String title, String community, String address, String price, String square, String description, String imgHref, String houseType, String rentWay, String websiteName, int attribute, String releaseTime, Date createTime, Date updateTime) {
        this.ruleName = ruleName;
        this.allowedDomains = allowedDomains;
        this.loopStart = loopStart;
        this.nextUrl = nextUrl;
        this.detailUrl = detailUrl;
        this.region = region;
        this.title = title;
        this.community = community;
        this.address = address;
        this.price = price;
        this.square = square;
        this.description = description;
        this.imgHref = imgHref;
        this.houseType = houseType;
        this.rentWay = rentWay;
        this.websiteName = websiteName;
        this.attribute = attribute;
        this.releaseTime = releaseTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public String getAllowedDomains() {
        return allowedDomains;
    }

    public void setAllowedDomains(String allowedDomains) {
        this.allowedDomains = allowedDomains;
    }

    public String getLoopStart() {
        return loopStart;
    }

    public void setLoopStart(String loopStart) {
        this.loopStart = loopStart;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getRentWay() {
        return rentWay;
    }

    public void setRentWay(String rentWay) {
        this.rentWay = rentWay;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
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
