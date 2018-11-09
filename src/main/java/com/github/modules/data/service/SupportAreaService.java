package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.data.entity.SupportAreaEntity;

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

}
