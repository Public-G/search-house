package com.github.modules.search.form;

import com.github.common.constant.SysConstant;
import com.github.modules.base.form.PageForm;

import java.io.Serializable;
import java.util.Date;

/**
 * 搜房请求参数结构体
 *
 * @author ZEALER
 * @date 2018-11-4
 */
public class HouseForm extends PageForm implements Serializable {
    private static final long serialVersionUID = 113452159L;

    private String city = SysConstant.DEFAULT_CITY;

    private String region;

    private String priceBlock;

    private String squareBlock;

    private String houseType;

    private String rentWay;

    private String sourceWebsite;

    public HouseForm() {

    }

    public HouseForm(String city) {
        this.city = city;
    }

    public HouseForm(int curr, int limit, String city, String region, String priceBlock, String squareBlock, String houseType, String rentWay, String sourceWebsite) {
        super(curr, limit);
        this.city = city;
        this.region = region;
        this.priceBlock = priceBlock;
        this.squareBlock = squareBlock;
        this.houseType = houseType;
        this.rentWay = rentWay;
        this.sourceWebsite = sourceWebsite;
    }

    public HouseForm(int curr, int limit, String keyword, String city, String region, String priceBlock, String squareBlock, String houseType, String rentWay, String sourceWebsite) {
        super(curr, limit, keyword);
        this.city = city;
        this.region = region;
        this.priceBlock = priceBlock;
        this.squareBlock = squareBlock;
        this.houseType = houseType;
        this.rentWay = rentWay;
        this.sourceWebsite = sourceWebsite;
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

    public String getPriceBlock() {
        return priceBlock;
    }

    public void setPriceBlock(String priceBlock) {
        this.priceBlock = priceBlock;
    }

    public String getSquareBlock() {
        return squareBlock;
    }

    public void setSquareBlock(String squareBlock) {
        this.squareBlock = squareBlock;
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

    public String getSourceWebsite() {
        return sourceWebsite;
    }

    public void setSourceWebsite(String sourceWebsite) {
        this.sourceWebsite = sourceWebsite;
    }
}
