package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.SpiderEntity;

/**
 * 爬虫项目管理
 *
 * @author ZEALER
 * @date 2018-11-12
 */
public interface SpiderService {

    PageUtils findPage(PageForm pageForm);

    /**
     * 保存爬虫项目
     */
    void save(SpiderEntity spiderEntity);

    /**
     * 修改爬虫项目
     */
    void update(SpiderEntity spiderEntity);

    /**
     * 根据ID查询爬虫项目
     */
    SpiderEntity findBySpiderId(Long spiderId);

    /**
     * 批量删除爬虫项目
     */
    void deleteBatch(Long[] spiderIds);
}
