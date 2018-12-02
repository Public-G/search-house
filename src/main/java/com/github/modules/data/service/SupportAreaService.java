package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.pojo.BaiduMapLocation;

import java.util.List;
import java.util.Map;


/**
 * 支持区域
 *
 * @author ZEALER
 * @date 2018-10-31
 */
public interface SupportAreaService {

    /**
     * 分页查询
     */
    PageUtils findPage(Map<String, String> params);

    /**
     * 查询所有支持的城市
     */
    List<SupportAreaEntity> findAllCity();

    /**
     * 根据城市查询区域
     */
    List<String> findRegionByCity(String cityName);

    /**
     * 根据级别查询区域
     */
    List<SupportAreaEntity> findByLevel(int level);

    /**
     * 根据名称查询
     */
    SupportAreaEntity findByCnName(String cnName);

    /**
     * 根据ID查询区域
     */
    SupportAreaEntity findByAreaId(Long areaId);

    /**
     * 保存
     */
    void save(List<SupportAreaEntity> areaEntityList);

    /**
     * 根据城市以及具体地位获取百度地图的经纬度
     */
    BaiduMapLocation getBaiduMapLocation(String city, String region, String address, String community);


}
