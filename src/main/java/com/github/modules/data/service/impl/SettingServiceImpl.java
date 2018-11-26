package com.github.modules.data.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.ScheduleJobEntity;
import com.github.modules.data.entity.SettingEntity;
import com.github.modules.data.repository.SettingRepository;
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

@Service("settingService")
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private SpiderService spiderService;

    @Override
    public PageUtils findPage(PageForm pageForm) {
        PageRequest pageable = new PageRequest(pageForm.getCurr() - 1, pageForm.getLimit());

        Page<SettingEntity> settingEntityPage;

        if (StringUtils.isNotBlank(pageForm.getKeyword())) {
            Specification<SettingEntity> specification = new Specification<SettingEntity>() {

                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<SettingEntity> root, CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    Path keywordPath = root.get("settingName");
                    return criteriaBuilder.like(keywordPath, "%" + pageForm.getKeyword() + "%");
                }
            };
            settingEntityPage = settingRepository.findAll(specification, pageable);

        } else {
            settingEntityPage = settingRepository.findAll(pageable);
        }

        return new PageUtils(settingEntityPage);
    }

    @Override
    public List<SettingEntity> findAll() {
        return settingRepository.findAll();
    }

    @Override
    public SettingEntity findById(Long id) {
        return settingRepository.findOne(id);
    }

    @Override
    public SettingEntity findByName(String name) {
        return settingRepository.findBySettingName(name);
    }

    @Transactional
    @Override
    public void save(SettingEntity entity) {
        entity.setCreateTime(new Date());
        settingRepository.save(entity);
    }

    @Transactional
    @Override
    public void update(SettingEntity entity) {
        entity.setUpdateTime(new Date());
        settingRepository.save(entity);
    }

    @Transactional
    @Override
    public void deleteBatch(Long[] settingIds) {
        settingRepository.deleteBySettingIdIn(settingIds);

        // 删除参数与项目关联
        spiderService.deleteSettingBatch(settingIds);
    }

}
