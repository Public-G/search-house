package com.github.modules.search.service;

import com.github.modules.search.dto.HouseBucketDTO;

import java.util.List;

/**
 * 地图找房
 *
 * @author ZEALER
 * @date 2018-11-20
 */
public interface MapService {

    /**
     * 聚合城市数据
     * @param city
     * @return
     */
    List<HouseBucketDTO> mapAggsByCity(String city);
}
