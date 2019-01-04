package com.github.modules.sys.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.sys.dto.SysUserDTO;
import com.github.modules.sys.entity.SysRoleEntity;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.repository.SysRoleRepository;
import com.github.modules.sys.service.SysRoleService;
import com.github.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public PageUtils findPage(PageForm pageForm) {
        String  keyword = pageForm.getKeyword();
        Integer curr    = pageForm.getCurr();
        Integer limit   = pageForm.getLimit();

        Sort.Order  order    = new Sort.Order(Sort.Direction.DESC, "roleId");
        Sort        sort     = new Sort(order);
        PageRequest pageable = new PageRequest(curr - 1, limit, sort);

        Page<SysRoleEntity> sysRoleEntityPage;

        if (!StringUtils.isBlank(keyword)) {
            Specification<SysRoleEntity> specification = new Specification<SysRoleEntity>() {
                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<SysRoleEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path roleNamePath = root.get("roleName");
                    return criteriaBuilder.like(roleNamePath, "%" + keyword + "%");
                }
            };
            sysRoleEntityPage = sysRoleRepository.findAll(specification, pageable);
        } else {
            sysRoleEntityPage = sysRoleRepository.findAll(pageable);
        }

        List<SysRoleEntity> pageContent       = sysRoleEntityPage.getContent();
        List<SysRoleEntity> sysRoleEntityList = new ArrayList<>();
        for (SysRoleEntity sysRoleEntity : pageContent) {
            // 设置创建者名称
            Long createUserId = sysRoleEntity.getCreateUserId();
            if (createUserId != null && createUserId != 0) {
                SysUserEntity userEntity = sysUserService.findById(createUserId);
                sysRoleEntity.setCreateUserName(userEntity.getUsername());
            }
            sysRoleEntityList.add(sysRoleEntity);
        }

        return new PageUtils(sysRoleEntityPage, sysRoleEntityList);
    }

    @Override
    public SysRoleEntity findById(Long id) {
        return sysRoleRepository.findOne(id);
    }

    @Override
    public SysRoleEntity findByName(String name) {
        return sysRoleRepository.findByRoleName(name);
    }

    @Transactional
    @Override
    public void save(SysRoleEntity sysRoleEntity) {
        sysRoleEntity.setCreateTime(new Date());
        sysRoleRepository.save(sysRoleEntity);
    }

    @Transactional
    @Override
    public void update(SysRoleEntity sysRoleEntity) {
        sysRoleRepository.save(sysRoleEntity);
    }

    @Transactional
    @Override
    public void deleteBatch(Long[] ids) {
        sysRoleRepository.deleteByRoleIdIn(ids);
    }
}
