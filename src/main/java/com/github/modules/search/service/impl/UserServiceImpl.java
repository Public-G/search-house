package com.github.modules.search.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.search.entity.UserEntity;
import com.github.modules.search.repository.UserRepository;
import com.github.modules.search.service.UserService;
import com.github.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    @Transactional
    @Override
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public PageUtils findPage(PageForm pageForm) {
        String  keyword = pageForm.getKeyword();
        Integer curr    = pageForm.getCurr();
        Integer limit   = pageForm.getLimit();

        Sort.Order  order    = new Sort.Order(Sort.Direction.DESC, "userId");
        Sort        sort     = new Sort(order);
        PageRequest pageable = new PageRequest(curr - 1, limit, sort);

        Page<UserEntity> userEntityPage;

        if (!StringUtils.isBlank(keyword)) {
            Specification<UserEntity> specification = new Specification<UserEntity>() {
                /**
                 * @param root            代表查询的实体类
                 * @param criteriaQuery   添加查询条件
                 * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path mobilePath = root.get("mobile");
                    return criteriaBuilder.like(mobilePath, "%" + keyword + "%");
                }
            };
            userEntityPage = userRepository.findAll(specification, pageable);
        } else {
            userEntityPage = userRepository.findAll(pageable);
        }

        return new PageUtils(userEntityPage);
    }
}
