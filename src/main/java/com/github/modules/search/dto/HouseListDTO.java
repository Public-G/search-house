package com.github.modules.search.dto;


import java.io.Serializable;

public class HouseListDTO implements Serializable {
    private static final long serialVersionUID = -12432434531L;

    private String sourceUrlId;

    private String title;

    private String imgHref;

    private String region;

    private String community;

    private Integer price;

    private String houseType;

    private String updateTime;

    public HouseListDTO() {
    }

    public HouseListDTO(String sourceUrlId, String title, String imgHref, String region, String community, Integer price, String houseType, String updateTime) {
        this.sourceUrlId = sourceUrlId;
        this.title = title;
        this.imgHref = imgHref;
        this.region = region;
        this.community = community;
        this.price = price;
        this.houseType = houseType;
        this.updateTime = updateTime;
    }

    public String getSourceUrlId() {
        return sourceUrlId;
    }

    public void setSourceUrlId(String sourceUrlId) {
        this.sourceUrlId = sourceUrlId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
