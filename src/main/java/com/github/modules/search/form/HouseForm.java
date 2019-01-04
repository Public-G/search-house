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

    private String region = "*";

    private String priceBlock;

    private String squareBlock;

    private String roomNum = "*";

    private String rentWay = "*";

    private String sourceWebsite = "*";

    private String orderBy = "_score";

    private String orderDirection = "desc";

    public HouseForm() {

    }

    public HouseForm(String city) {
        this.city = city;
    }

    public HouseForm(int curr, int limit, String city, String region, String priceBlock, String squareBlock, String roomNum, String rentWay, String sourceWebsite, String orderBy) {
        super(curr, limit);
        this.city = city;
        this.region = region;
        this.priceBlock = priceBlock;
        this.squareBlock = squareBlock;
        this.roomNum = roomNum;
        this.rentWay = rentWay;
        this.sourceWebsite = sourceWebsite;
        this.orderBy = orderBy;
    }

    public HouseForm(int curr, int limit, String keyword, String city, String region, String priceBlock, String squareBlock, String roomNum, String rentWay, String sourceWebsite, String orderBy) {
        super(curr, limit, keyword);
        this.city = city;
        this.region = region;
        this.priceBlock = priceBlock;
        this.squareBlock = squareBlock;
        this.roomNum = roomNum;
        this.rentWay = rentWay;
        this.sourceWebsite = sourceWebsite;
        this.orderBy = orderBy;
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

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
