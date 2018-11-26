package com.github.modules.data.service;

import com.github.modules.base.service.BaseService;
import com.github.modules.data.entity.SpiderEntity;

/**
 * 爬虫项目管理
 *
 * @author ZEALER
 * @date 2018-11-12
 */
public interface SpiderService extends BaseService<SpiderEntity> {

    void deleteRuleBatch(Long[] ruleIds);

    void deleteSettingBatch(Long[] settingIds);
}
