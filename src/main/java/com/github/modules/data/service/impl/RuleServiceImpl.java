package com.github.modules.data.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.repository.RuleRepository;
import com.github.modules.data.service.RuleService;
import com.github.modules.data.service.SupportAreaService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("ruleService")
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public PageUtils findPage(PageForm pageForm) {
        PageRequest pageable = new PageRequest(pageForm.getCurr() - 1, pageForm.getLimit());

        Page<RuleEntity> ruleEntityPage;

        if (StringUtils.isNotBlank(pageForm.getKeyword())) {
            Specification<RuleEntity> specification = new Specification<RuleEntity>() {
                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<RuleEntity> root, CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    Path keywordPath = root.get("ruleName");
                    return criteriaBuilder.like(keywordPath, "%" + pageForm.getKeyword() + "%");
                }
            };
            ruleEntityPage = ruleRepository.findAll(specification, pageable);

        } else {
            ruleEntityPage = ruleRepository.findAll(pageable);
        }

        return new PageUtils(ruleEntityPage);
    }

    @Transactional
    @Override
    public void save(RuleEntity ruleEntity) {
        ruleEntity.setCreateTime(new Date());
        ruleRepository.save(ruleEntity);
    }

    @Transactional
    @Override
    public void update(RuleEntity ruleEntity) {
        ruleEntity.setUpdateTime(new Date());
        ruleRepository.save(ruleEntity);
    }

    @Override
    public RuleEntity findByRuleId(Long ruleId) {
        return ruleRepository.findOne(ruleId);
    }

    @Transactional
    @Override
    public void deleteBatch(Long[] ruleIds) {
        ruleRepository.deleteByRuleIdIn(ruleIds);
    }

    @Override
    public List<RuleEntity> findAll() {
        return ruleRepository.findAll();
    }
}
