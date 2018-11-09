package com.github.modules.search.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class HouseDTO implements Serializable {
    private static final long serialVersionUID = 243243453L;

    private String id;

    @JsonProperty(value = "cityCnName")
    private String city_cn_name;

    @JsonProperty(value = "regionCnName")
    private String region_cn_name;

    private String title;

    private Double area;

    private Double price;

    @JsonProperty(value = "houseType")
    private String house_type;

    @JsonProperty(value = "rentWay")
    private String rent_way;

    private String community;

    private String address;

    private String description;

    @JsonProperty(value = "sourceUrl")
    private String source_url;

    @JsonProperty(value = "imgHref")
    private String img_href;

    @JsonProperty(value = "createTime")
    private String create_time;

    public HouseDTO() {

    }

    public HouseDTO(String id, String city_cn_name, String region_cn_name, String title, Double area, Double price, String house_type, String rent_way, String community, String address, String description, String source_url, String img_href, String create_time) {
        this.id = id;
        this.city_cn_name = city_cn_name;
        this.region_cn_name = region_cn_name;
        this.title = title;
        this.area = area;
        this.price = price;
        this.house_type = house_type;
        this.rent_way = rent_way;
        this.community = community;
        this.address = address;
        this.description = description;
        this.source_url = source_url;
        this.img_href = img_href;
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_cn_name() {
        return city_cn_name;
    }

    public void setCity_cn_name(String city_cn_name) {
        this.city_cn_name = city_cn_name;
    }

    public String getRegion_cn_name() {
        return region_cn_name;
    }

    public void setRegion_cn_name(String region_cn_name) {
        this.region_cn_name = region_cn_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getRent_way() {
        return rent_way;
    }

    public void setRent_way(String rent_way) {
        this.rent_way = rent_way;
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

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getImg_href() {
        return img_href;
    }

    public void setImg_href(String img_href) {
        this.img_href = img_href;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
