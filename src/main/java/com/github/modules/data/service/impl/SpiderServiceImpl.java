package com.github.modules.data.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.SpiderEntity;
import com.github.modules.data.repository.SpiderRepository;
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

@Service("spiderService")
public class SpiderServiceImpl implements SpiderService {

    @Autowired
    private SpiderRepository spiderRepository;

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

        return new PageUtils(spiderEntityPage);
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

    @Override
    public SpiderEntity findBySpiderId(Long spiderId) {
        return spiderRepository.findOne(spiderId);
    }

    @Transactional
    @Override
    public void deleteBatch(Long[] spiderIds) {
        spiderRepository.deleteBySpiderIdIn(spiderIds);
    }
}
