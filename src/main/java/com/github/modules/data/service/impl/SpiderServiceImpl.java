package com.github.modules.data.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.entity.SettingEntity;
import com.github.modules.data.entity.SpiderEntity;
import com.github.modules.data.repository.SpiderRepository;
import com.github.modules.data.service.RuleService;
import com.github.modules.data.service.SettingService;
import com.github.modules.data.service.SpiderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@Service("spiderService")
public class SpiderServiceImpl implements SpiderService {

    @Autowired
    private SpiderRepository spiderRepository;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private SettingService settingService;

    @Override
    public PageUtils findPage(PageForm pageForm) {
        PageRequest pageable = new PageRequest(pageForm.getCurr() - 1, pageForm.getLimit());

        Page<SpiderEntity> spiderEntityPage;

        if (StringUtils.isNotBlank(pageForm.getKeyword())) {
            Specification<SpiderEntity> specification = new Specification<SpiderEntity>() {
                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<SpiderEntity> root, CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    Path keywordPath = root.get("spiderName");
                    return criteriaBuilder.like(keywordPath, "%" + pageForm.getKeyword() + "%");
                }
            };
            spiderEntityPage = spiderRepository.findAll(specification, pageable);

        } else {
            spiderEntityPage = spiderRepository.findAll(pageable);
        }

        for (SpiderEntity spiderEntity : spiderEntityPage.getContent()) {
            if (spiderEntity.getRuleId() != null) {
                RuleEntity ruleEntity = ruleService.findById(spiderEntity.getRuleId());
                spiderEntity.setRuleName(ruleEntity.getRuleName());
            }

            if (spiderEntity.getSettingId() != null) {
                SettingEntity settingEntity = settingService.findById(spiderEntity.getSettingId());
                spiderEntity.setSettingName(settingEntity.getSettingName());
            }
        }

        return new PageUtils(spiderEntityPage);
    }

    @Override
    public SpiderEntity findById(Long spiderId) {
        return spiderRepository.findOne(spiderId);
    }

    @Override
    public SpiderEntity findByName(String name) {
        return spiderRepository.findBySpiderName(name);
    }

    @Transactional
    @Override
    public void save(SpiderEntity spiderEntity) {
        spiderEntity.setCreateTime(new Date());
        spiderRepository.save(spiderEntity);
    }

    @Transactional
    @Override
    public void update(SpiderEntity spiderEntity) {
        spiderEntity.setUpdateTime(new Date());
        spiderRepository.save(spiderEntity);
    }

    @Transactional
    @Override
    public void deleteBatch(Long[] spiderIds) {
        spiderRepository.deleteBySpiderIdIn(spiderIds);
    }

    @Override
    public List<SpiderEntity> findByRuleIdIn(Long[] ruleIds) {
        return spiderRepository.findByRuleIdIn(ruleIds);
    }

    @Override
    public List<SpiderEntity> findBySettingIdIn(Long[] settingIds) {
        return spiderRepository.findBySettingIdIn(settingIds);
    }

    @Override
    public List<String> findCity() {
        return spiderRepository.findCity();
    }
}
