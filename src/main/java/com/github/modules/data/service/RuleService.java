package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;

import java.util.List;

/**
 * 解析规则
 *
 * @author ZEALER
 * @date 2018-11-11
 */
public interface RuleService {

    PageUtils findPage(PageForm pageForm);

    /**
     * 保存解析规则
     */
    void save(RuleEntity ruleEntity);

    /**
     * 修改解析规则
     */
    void update(RuleEntity ruleEntity);

    /**
     * 根据ID查询解析规则
     */
    RuleEntity findByRuleId(Long ruleId);

    /**
     * 批量删除解析规则
     */
    void deleteBatch(Long[] ruleIds);

    List<RuleEntity> findAll();
}
