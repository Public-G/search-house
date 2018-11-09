package com.github.modules.data.service.impl;

import com.github.common.constant.SysConstant;
import com.github.common.utils.PageUtils;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.repository.SupportAreaRepository;
import com.github.modules.data.service.SupportAreaService;
import com.github.modules.sys.service.impl.SysMenuServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * 支持区域
 *
 * @author ZEALER
 * @date 2018-10-31
 */
import static com.github.common.constant.SysConstant.RequestParam.*;

@Service("supportAreaService")
public class SupportAreaServiceImpl implements SupportAreaService {

    @Autowired
    private SupportAreaRepository supportAreaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PageUtils findPage(Map<String, String> params) {
        String      keyword  = params.get(KEYWORD.getName());
        Integer     curr     = Integer.valueOf(params.get(CURR.getName()));
        Integer     limit    = Integer.valueOf(params.get(LIMIT.getName()));
        PageRequest pageable = new PageRequest(curr - 1, limit);
        Long        parentId = Long.valueOf(params.get("parentId"));

        Page<SupportAreaEntity> supportAreaEntityPage;

        Specification<SupportAreaEntity> specification = new Specification<SupportAreaEntity>() {
            /**
             * @param root            代表查询的实体类
             * @param criteriaQuery   添加查询条件
             * @param criteriaBuilder 用于创建 Criteria 相关对象的工厂,可以从中获取到 Predicate 对象(查询条件对象)
             * @return
             */
            @Override
            public Predicate toPredicate(Root<SupportAreaEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate;
                if (!StringUtils.isBlank(keyword)) {
                    predicate = criteriaBuilder.like(root.get("cnName"), "%" + keyword + "%");
                }
                predicate = criteriaBuilder.equal(root.get("parentId"), parentId);
                return predicate;
            }
        };
        supportAreaEntityPage = supportAreaRepository.findAll(specification, pageable);

        return new PageUtils(supportAreaEntityPage);
    }

    /**
     * 获取城市列表
     * 参考：{@link SysMenuServiceImpl}
     *
     * @return supportAreaListWrapper
     */
    @Override
    public List<SupportAreaEntity> findAllCity() {
        List<SupportAreaEntity> supportAreaListWrapper = new ArrayList<>();

        List<SupportAreaEntity> allCity = supportAreaRepository.findAll();

        Map<Long, SupportAreaEntity> supportAreaMap = new HashMap<>();
        for (SupportAreaEntity supportAreaEntity : allCity) {
            supportAreaMap.put(supportAreaEntity.getId(), supportAreaEntity);
        }

        for (SupportAreaEntity supportAreaEntity : allCity) {
            if (supportAreaEntity.getLevel() == SysConstant.AreaLevel.CITYPREFIX.getLevel()) {
                supportAreaListWrapper.add(supportAreaEntity);

            } else {
                if (supportAreaEntity.getLevel() != SysConstant.AreaLevel.REGION.getLevel()) {
                    Long              parentId   = supportAreaEntity.getParentId();
                    SupportAreaEntity areaParent = supportAreaMap.get(parentId);

                    List<SupportAreaEntity> childCity = areaParent.getChildCity();
                    if (childCity != null) {
                        childCity.add(supportAreaEntity);

                    } else {
                        childCity = new ArrayList<>();
                        childCity.add(supportAreaEntity);

                        areaParent.setChildCity(childCity);
                    }
                }
            }

        }
        return supportAreaListWrapper;
    }

    @Override
    public List<String> findRegionByCity(String cityName) {
        SupportAreaEntity       cityEntity          = supportAreaRepository.findByCnName(cityName);
        List<SupportAreaEntity> allRegionEntityList = supportAreaRepository.findByParentId(cityEntity.getId());

        List<String> regionArray = new ArrayList<>();
        for (SupportAreaEntity supportAreaEntity : allRegionEntityList) {
            regionArray.add(supportAreaEntity.getCnName());
        }
        return regionArray;
    }
}
