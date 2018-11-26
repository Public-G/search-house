package com.github.modules.base.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 百度位置信息
 *
 * @author ZEALER
 * @date 2018-11-15
 */
public class BaiduMapLocation {

    // 经度
    @JsonProperty("lon")
    private double longitude;

    // 纬度
    @JsonProperty("lat")
    private double latitude;

    public BaiduMapLocation() {
    }

    public BaiduMapLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
