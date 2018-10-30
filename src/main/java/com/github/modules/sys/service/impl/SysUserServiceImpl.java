package com.github.modules.sys.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.sys.dto.SysUserDTO;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.repository.SysUserRepository;
import com.github.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * 系统用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PageUtils findPage(Map<String, String> params) {
        String      username = params.get("username");
        Integer     curr     = Integer.valueOf(params.get("curr"));
        Integer     limit    = Integer.valueOf(params.get("limit"));
        PageRequest pageable = new PageRequest(curr - 1, limit);

        Page<SysUserEntity> sysUserEntityPage;

        if (!StringUtils.isBlank(username)) {
            Specification<SysUserEntity> specification = new Specification<SysUserEntity>() {
                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<SysUserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path usernamePath = root.get("username");
                    return criteriaBuilder.like(usernamePath, "%" + username + "%");
                }
            };
            sysUserEntityPage = sysUserRepository.findAll(specification, pageable);
        } else {
            sysUserEntityPage = sysUserRepository.findAll(pageable);
        }

        List<SysUserEntity> pageContent    = sysUserEntityPage.getContent();
        List<SysUserDTO>    pageContentDTO = new ArrayList<>();
        for (SysUserEntity sysUserEntity : pageContent) {
            SysUserDTO sysUserDTO = modelMapper.map(sysUserEntity, SysUserDTO.class);
            pageContentDTO.add(sysUserDTO);
        }

        return new PageUtils(sysUserEntityPage, pageContentDTO);
    }

    @Override
    public SysUserEntity findByUserId(Long userId) {
        return sysUserRepository.findByUserId(userId);
    }

    @Override
    public SysUserEntity findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }


    @Override
    public List<Long> findAllMenuId(Long userId) {
        return sysUserRepository.findAllMenuId(userId);
    }

    @Override
    @Transactional
    public void userSaveOrUpdate(SysUserEntity sysUserEntity) {
        if (StringUtils.isBlank(sysUserEntity.getPassword())) {
            sysUserEntity.setPassword(encodePassword());
            sysUserEntity.setCreateTime(new Date());
        }

        sysUserRepository.save(sysUserEntity);
    }

    @Override
    @Transactional
    public void deleteBatch(Collection<Long> userIds) {
        sysUserRepository.deleteByUserIdIn(userIds);
    }

    @Override
    @Transactional
    public void reset(Long userId) {
        String resetPassword = encodePassword();
        sysUserRepository.updatePassword(userId, resetPassword);
    }

    private String encodePassword() {
        return passwordEncoder.encode("123456");
    }

}
