package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.data.pojo.HouseIndexTemplate;

import java.util.Map;

public interface HouseService {

    /**
     * 查询房源
     * @param params
     * @return
     */
    PageUtils findPage(Map<String, String> params);

    /**
     * 保存或更新房源
     */
    void saveOrUpdate(HouseIndexTemplate houseIndexTemplate);
}
