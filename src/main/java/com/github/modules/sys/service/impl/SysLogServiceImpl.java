package com.github.modules.sys.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.sys.entity.SysLogEntity;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.repository.SysLogRepository;
import com.github.modules.sys.service.SysLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogRepository sysLogRepository;

    @Override
    public PageUtils findPage(PageForm pageForm) {
        String  keyword = pageForm.getKeyword();
        Integer curr    = pageForm.getCurr();
        Integer limit   = pageForm.getLimit();

        Sort.Order  order    = new Sort.Order(Sort.Direction.DESC, "logId");
        Sort        sort     = new Sort(order);
        PageRequest pageable = new PageRequest(curr - 1, limit, sort);

        Page<SysLogEntity> sysLogEntityPage;

        if (!StringUtils.isBlank(keyword)) {
            Specification<SysLogEntity> specification = new Specification<SysLogEntity>() {
                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<SysLogEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path path = root.get("username");
                    return criteriaBuilder.like(path, "%" + keyword + "%");
                }
            };
            sysLogEntityPage = sysLogRepository.findAll(specification, pageable);
        } else {
            sysLogEntityPage = sysLogRepository.findAll(pageable);
        }

        return new PageUtils(sysLogEntityPage);
    }

    @Transactional
    @Override
    public void save(SysLogEntity sysLogEntity) {
        sysLogRepository.save(sysLogEntity);
    }
}
