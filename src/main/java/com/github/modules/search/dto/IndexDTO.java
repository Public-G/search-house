package com.github.modules.search.dto;

import java.io.Serializable;

/**
 * 首页要展示的数据
 *
 * @author ZEALER
 * @date 2018-12-02
 */
public class IndexDTO implements Serializable {

    private static final long serialVersionUID = 8811432434531L;

    /**
     * 城市
     */
    private String city;

    /**
     * 房源总数
     */
    private Long houseCount;

    /**
     * 当前城市房源总数
     */
    private Long cityHouseCount;

    /**
     * 本周更新
     */
    private Long houseWeekCount;

    /**
     * 来源网站
     */
    private Integer websiteCount;

    /**
     * 当前城市租金平均价格
     */
    private Long avgPrice;

    /**
     * 当前城市租金在全国排名
     */
    private Integer top;

    public IndexDTO() {
    }

    public IndexDTO(Long houseCount, Long houseWeekCount, Integer websiteCount, Long avgPrice, Integer top) {
        this.houseCount = houseCount;
        this.houseWeekCount = houseWeekCount;
        this.websiteCount = websiteCount;
        this.avgPrice = avgPrice;
        this.top = top;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(Long houseCount) {
        this.houseCount = houseCount;
    }

    public Long getCityHouseCount() {
        return cityHouseCount;
    }

    public void setCityHouseCount(Long cityHouseCount) {
        this.cityHouseCount = cityHouseCount;
    }

    public Long getHouseWeekCount() {
        return houseWeekCount;
    }

    public void setHouseWeekCount(Long houseWeekCount) {
        this.houseWeekCount = houseWeekCount;
    }

    public Integer getWebsiteCount() {
        return websiteCount;
    }

    public void setWebsiteCount(Integer websiteCount) {
        this.websiteCount = websiteCount;
    }

    public Long getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Long avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }
}
