package com.github.modules.data.service.impl;

import com.github.common.constant.SysConstant;
import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.ScheduleJobEntity;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.repository.ScheduleJobRepository;
import com.github.modules.data.service.ScheduleJobService;
import com.github.modules.data.utils.ScheduleUtils;
import com.github.modules.sys.dto.SysUserDTO;
import com.github.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.github.common.constant.SysConstant.RequestParam.CURR;
import static com.github.common.constant.SysConstant.RequestParam.KEYWORD;
import static com.github.common.constant.SysConstant.RequestParam.LIMIT;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleJobRepository scheduleJobRepository;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init(){
        List<ScheduleJobEntity> scheduleJobList = scheduleJobRepository.findAll();

        if (scheduleJobList != null) {
            for(ScheduleJobEntity scheduleJobEntity : scheduleJobList){
                CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJobEntity.getJobId());
                //如果不存在，则创建
                if(cronTrigger == null) {
                    ScheduleUtils.createScheduleJob(scheduler, scheduleJobEntity);
                }else {
                    ScheduleUtils.updateScheduleJob(scheduler, scheduleJobEntity);
                }
            }
        }
    }

    @Override
    public PageUtils findPage(PageForm pageForm) {
        PageRequest pageable = new PageRequest(pageForm.getCurr() - 1, pageForm.getLimit());

        Page<ScheduleJobEntity> scheduleJobEntityPage;

        if (StringUtils.isNotBlank(pageForm.getKeyword())) {
            Specification<ScheduleJobEntity> specification = new Specification<ScheduleJobEntity>() {
                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<ScheduleJobEntity> root, CriteriaQuery<?> criteriaQuery,
                                             CriteriaBuilder criteriaBuilder) {
                    Path keywordPath = root.get("beanName");
                    return criteriaBuilder.like(keywordPath, "%" + pageForm.getKeyword() + "%");
                }
            };
            scheduleJobEntityPage = scheduleJobRepository.findAll(specification, pageable);

        } else {
            scheduleJobEntityPage = scheduleJobRepository.findAll(pageable);
        }

        return new PageUtils(scheduleJobEntityPage);
    }

    @Override
    public ScheduleJobEntity findByJobId(Long jobId) {
        return scheduleJobRepository.findOne(jobId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScheduleJobEntity scheduleJobEntity) {
        scheduleJobEntity.setCreateTime(new Date());
        ScheduleJobEntity jobEntity = scheduleJobRepository.save(scheduleJobEntity);

        ScheduleUtils.createScheduleJob(scheduler, jobEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleJobEntity scheduleJobEntity) {
        scheduleJobEntity.setUpdateTime(new Date());
        ScheduleJobEntity jobEntity = scheduleJobRepository.save(scheduleJobEntity);

        ScheduleUtils.updateScheduleJob(scheduler, jobEntity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] jobIds) {
        scheduleJobRepository.deleteByJobIdIn(Arrays.asList(jobIds));

        for(Long jobId : jobIds){
            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(Long[] jobIds) {
        for(Long jobId : jobIds){
            ScheduleUtils.run(scheduler, scheduleJobRepository.findOne(jobId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(Long[] jobIds) {
        for(Long jobId : jobIds){
            scheduleJobRepository.updateStatus(jobId, SysConstant.ScheduleStatus.PAUSE.getValue());

            ScheduleUtils.pauseJob(scheduler, jobId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(Long[] jobIds) {
        for(Long jobId : jobIds){
            scheduleJobRepository.updateStatus(jobId, SysConstant.ScheduleStatus.NORMAL.getValue());

            ScheduleUtils.resumeJob(scheduler, jobId);
        }
    }

}
