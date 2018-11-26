package com.github.modules.data.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.base.service.BaseService;
import com.github.modules.data.entity.RuleEntity;

import java.util.List;

/**
 * 解析规则
 *
 * @author ZEALER
 * @date 2018-11-11
 */
public interface RuleService extends BaseService<RuleEntity> {

    List<RuleEntity> findAll();

}
