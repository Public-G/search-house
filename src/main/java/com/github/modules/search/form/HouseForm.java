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

    private String cityCnName = SysConstant.DEFAULT_CITY;

    private String regionCnName;

    private String priceBlock;

    private String areaBlock;

    private String houseType;

    private String rentWay;

    private String sourceWebsite;

    public HouseForm() {

    }

    public HouseForm(String cityCnName) {
        this.cityCnName = cityCnName;
    }

    public HouseForm(int curr, int limit, String cityCnName, String regionCnName, String priceBlock, String areaBlock, String houseType, String rentWay, String sourceWebsite) {
        super(curr, limit);
        this.cityCnName = cityCnName;
        this.regionCnName = regionCnName;
        this.priceBlock = priceBlock;
        this.areaBlock = areaBlock;
        this.houseType = houseType;
        this.rentWay = rentWay;
        this.sourceWebsite = sourceWebsite;
    }

    public HouseForm(int curr, int limit, String keyword, String cityCnName, String regionCnName, String priceBlock, String areaBlock, String houseType, String rentWay, String sourceWebsite) {
        super(curr, limit, keyword);
        this.cityCnName = cityCnName;
        this.regionCnName = regionCnName;
        this.priceBlock = priceBlock;
        this.areaBlock = areaBlock;
        this.houseType = houseType;
        this.rentWay = rentWay;
        this.sourceWebsite = sourceWebsite;
    }

    public String getCityCnName() {
        return cityCnName;
    }

    public void setCityCnName(String cityCnName) {
        this.cityCnName = cityCnName;
    }

    public String getRegionCnName() {
        return regionCnName;
    }

    public void setRegionCnName(String regionCnName) {
        this.regionCnName = regionCnName;
    }

    public String getPriceBlock() {
        return priceBlock;
    }

    public void setPriceBlock(String priceBlock) {
        this.priceBlock = priceBlock;
    }

    public String getAreaBlock() {
        return areaBlock;
    }

    public void setAreaBlock(String areaBlock) {
        this.areaBlock = areaBlock;
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
