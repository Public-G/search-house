package com.github.modules.data.service;

import com.github.common.utils.PageUtils;

import java.util.Map;

public interface HouseService {

    /**
     * 查询房源
     * @param params
     * @return
     */
    PageUtils findPage(Map<String, String> params);


}
