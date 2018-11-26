package com.github.modules.base.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 房源索引结构模板
 *
 * @author ZEALER
 * @date 2018-11-15
 */
public class HouseIndexTemplate {

    @NotBlank(message = "城市不能为空")
    private String city;

    private String region;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotNull(message = "价格不能为空")
    private Integer price;

    @NotNull(message = "面积不能为空")
    private Integer square;

    private String houseType;

    private Integer roomNum;

    private int rentWay;

    private String community;

    @NotBlank(message = "详细地址不能为空")
    private String address;

    private String description;

    private String imgHref;

    @NotBlank(message = "sourceUrlId不能为空")
    private String sourceUrlId;

    @NotBlank(message = "sourceUrl不能为空")
    private String sourceUrl;

    @NotNull(message = "来源网站不能为空")
    private Long website;

    @NotNull(message = "数据标识(中介/个人)不能为空")
    private int attribute;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private List<HouseSuggest> suggest;

    private BaiduMapLocation location;

    public HouseIndexTemplate() {
    }

    public HouseIndexTemplate(String city, String region, String title, Integer price, Integer square, String houseType, Integer roomNum, int rentWay, String community, String address, String description, String imgHref, String sourceUrlId, String sourceUrl, Long website, int attribute, Date updateTime, Date createTime, List<HouseSuggest> suggest, BaiduMapLocation location) {
        this.city = city;
        this.region = region;
        this.title = title;
        this.price = price;
        this.square = square;
        this.houseType = houseType;
        this.roomNum = roomNum;
        this.rentWay = rentWay;
        this.community = community;
        this.address = address;
        this.description = description;
        this.imgHref = imgHref;
        this.sourceUrlId = sourceUrlId;
        this.sourceUrl = sourceUrl;
        this.website = website;
        this.attribute = attribute;
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.suggest = suggest;
        this.location = location;
    }

    public HouseIndexTemplate(String city, String region, String title, Integer price, Integer square, String houseType, Integer roomNum, int rentWay, String community, String address, String description, String imgHref, String sourceUrlId, String sourceUrl, Long website, int attribute, Date updateTime, Date createTime) {
        this.city = city;
        this.region = region;
        this.title = title;
        this.price = price;
        this.square = square;
        this.houseType = houseType;
        this.roomNum = roomNum;
        this.rentWay = rentWay;
        this.community = community;
        this.address = address;
        this.description = description;
        this.imgHref = imgHref;
        this.sourceUrlId = sourceUrlId;
        this.sourceUrl = sourceUrl;
        this.website = website;
        this.attribute = attribute;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSquare() {
        return square;
    }

    public void setSquare(Integer square) {
        this.square = square;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public int getRentWay() {
        return rentWay;
    }

    public void setRentWay(int rentWay) {
        this.rentWay = rentWay;
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

    public String getSourceUrlId() {
        return sourceUrlId;
    }

    public void setSourceUrlId(String sourceUrlId) {
        this.sourceUrlId = sourceUrlId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Long getWebsite() {
        return website;
    }

    public void setWebsite(Long website) {
        this.website = website;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<HouseSuggest> getSuggest() {
        return suggest;
    }

    public void setSuggest(List<HouseSuggest> suggest) {
        this.suggest = suggest;
    }

    public BaiduMapLocation getLocation() {
        return location;
    }

    public void setLocation(BaiduMapLocation location) {
        this.location = location;
    }
}
