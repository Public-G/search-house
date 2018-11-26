package com.github.modules.data.service;

import com.github.modules.base.pojo.BaiduMapLocation;

/**
 * 百度地图相关接口
 *
 * @author ZEALER
 * @date 2018-11-20
 */
public interface BaiduMapService {

    /**
     * 根据城市以及具体地位获取百度地图的经纬度
     */
    BaiduMapLocation getBaiduMapLocation(String city, String region, String address, String community);

    /**
     * 上传百度LBS数据
     */
    void uploadLBS(BaiduMapLocation location, String title, String address,
                   String sourceUrlId, Integer price, Integer square);

    /**
     * 移除百度LBS数据
     */
    void removeLBS(String sourceUrlId);

}
