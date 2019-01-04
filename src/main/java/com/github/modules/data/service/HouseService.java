package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.pojo.HouseIndexTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HouseService {

    /**
     * 查询房源
     * @param pageForm
     * @return
     */
    PageUtils findPage(PageForm pageForm);

    /**
     * 根据ID查询房源信息
     * @param sourceUrlId
     * @return
     */
    HouseIndexTemplate findById(String sourceUrlId);

    /**
     * 批量查询房源信息
     * @param sourceUrlId
     * @return
     */
    List<HouseIndexTemplate> multiGet(String ... sourceUrlId);

    /**
     * 滚动搜索所有房源信息的ID
     * @return
     */
    Set<String> scrollGetIds();

    /**
     * 保存或更新房源
     */
    void saveOrUpdate(HouseIndexTemplate houseIndexTemplate);

    /**
     * 更新房源（后台管理）
     */
    void update(HouseIndexTemplate houseIndexTemplate);

    /**
     * 删除房源信息
     * @param sourceUrlId
     */
    void delete(String ... sourceUrlId);
}
