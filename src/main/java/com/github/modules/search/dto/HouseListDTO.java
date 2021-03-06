package com.github.modules.search.dto;


import java.io.Serializable;

public class HouseListDTO implements Serializable {
    private static final long serialVersionUID = -12432434531L;

    protected String sourceUrlId;

    protected String title;

    protected String imgHref;

    protected String region;

    protected String community;

    protected Integer price;

    protected String houseType;

    protected String releaseTime;

    public HouseListDTO() {
    }

    public HouseListDTO(String sourceUrlId, String title, String imgHref, String region, String community, Integer price, String houseType, String releaseTime) {
        this.sourceUrlId = sourceUrlId;
        this.title = title;
        this.imgHref = imgHref;
        this.region = region;
        this.community = community;
        this.price = price;
        this.houseType = houseType;
        this.releaseTime = releaseTime;
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

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }
}
