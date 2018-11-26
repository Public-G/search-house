package com.github.modules.search.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.print.attribute.standard.MediaSize;
import java.io.Serializable;
import java.util.Date;

public class HouseDTO extends HouseListDTO implements Serializable {
    private static final long serialVersionUID = 243243453L;

    private String city;

    private String title;

    private Integer square;

    private String rentWay;

    private String address;

    private String sourceUrl;

    private String description;


    public HouseDTO() {
        super();
    }

    public HouseDTO(String sourceUrlId, String title, String imgHref, String region, String community, Integer price, String houseType, String updateTime, String city, String title1, Integer square, String rentWay, String address, String sourceUrl, String description1) {
        super(sourceUrlId, title, imgHref, region, community, price, houseType, updateTime);
        this.city = city;
        this.title = title1;
        this.square = square;
        this.rentWay = rentWay;
        this.address = address;
        this.sourceUrl = sourceUrl;
        this.description = description1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSquare() {
        return square;
    }

    public void setSquare(Integer square) {
        this.square = square;
    }

    public String getRentWay() {
        return rentWay;
    }

    public void setRentWay(String rentWay) {
        this.rentWay = rentWay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
