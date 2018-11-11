package com.github.modules.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class RuleDTO implements Serializable {
    private static final long serialVersionUID = -613882152L;

    private Long ruleId;

    private String ruleName;

    private String allowedDomains;

    private String loopStart;

    private String nextUrl;

    private String detailUrl;

    private String region;

    private String title;

    private String community;

    private String address;

    private String price;

    private String square;

    private String description;

    private String imgHref;

    private String houseType;

    private String rentWay;

    private Date createTime;

    @JsonProperty("cityCnName")
    private String cityId;

    public RuleDTO() {
    }

    public RuleDTO(Long ruleId, String ruleName, String allowedDomains, String loopStart, String nextUrl, String detailUrl, String region, String title, String community, String address, String price, String square, String description, String imgHref, String houseType, String rentWay, Date createTime, String cityId) {
        this.ruleId = ruleId;
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
        this.createTime = createTime;
        this.cityId = cityId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
