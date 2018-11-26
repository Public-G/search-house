package com.github.modules.data.service;

import com.github.modules.base.service.BaseService;
import com.github.modules.data.entity.SettingEntity;

import java.util.List;

/**
 * 爬虫参数设置
 *
 * @author ZEALER
 * @date 2018-11-23
 */
public interface SettingService extends BaseService<SettingEntity> {

    List<SettingEntity> findAll();

}
