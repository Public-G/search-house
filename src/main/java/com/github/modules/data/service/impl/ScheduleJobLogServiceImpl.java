package com.github.modules.data.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.ScheduleJobEntity;
import com.github.modules.data.entity.ScheduleJobLogEntity;
import com.github.modules.data.repository.ScheduleJobLogRepository;
import com.github.modules.data.service.ScheduleJobLogService;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {

    @Autowired
    private ScheduleJobLogRepository scheduleJobLogRepository;


    @Override
    public PageUtils findPage(PageForm pageForm) {
        PageRequest pageable = new PageRequest(pageForm.getCurr() - 1, pageForm.getLimit());

        Page<ScheduleJobLogEntity> scheduleJobEntityPage;

        if (StringUtils.isNotBlank(pageForm.getKeyword())) {
            Specification<ScheduleJobLogEntity> specification = new Specification<ScheduleJobLogEntity>() {
                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<ScheduleJobLogEntity> root, CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    Path keywordPath = root.get("jobId");
                    return criteriaBuilder.equal(keywordPath, pageForm.getKeyword());
                }
            };
            scheduleJobEntityPage = scheduleJobLogRepository.findAll(specification, pageable);

        } else {
            scheduleJobEntityPage = scheduleJobLogRepository.findAll(pageable);
        }

        return new PageUtils(scheduleJobEntityPage);
    }

    @Transactional
    @Override
    public void save(ScheduleJobLogEntity scheduleJobLogEntity) {
        scheduleJobLogRepository.save(scheduleJobLogEntity);
    }
}
