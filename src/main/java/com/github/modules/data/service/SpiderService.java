package com.github.modules.data.service;

import com.github.modules.base.service.BaseService;
import com.github.modules.data.entity.SpiderEntity;

import java.util.List;

/**
 * 爬虫项目管理
 *
 * @author ZEALER
 * @date 2018-11-12
 */
public interface SpiderService extends BaseService<SpiderEntity> {

    /**
     * 根据规则ID查询项目
     */
    List<SpiderEntity> findByRuleIdIn(Long[] ruleIds);

    /**
     * 根据参数ID查询项目
     */
    List<SpiderEntity> findBySettingIdIn(Long[] settingIds);

    /**
     * 查询所有城市
     */
    List<String> findCity();
}
