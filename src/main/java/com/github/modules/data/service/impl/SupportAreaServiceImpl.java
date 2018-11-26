package com.github.modules.data.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.constant.SysConstant;
import com.github.common.utils.PageUtils;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.base.pojo.BaiduMapLocation;
import com.github.modules.data.repository.SupportAreaRepository;
import com.github.modules.data.service.SupportAreaService;
import com.github.modules.sys.service.impl.SysMenuServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SupportAreaRepository supportAreaRepository;

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private HttpClient httpClient;

    private static final String BAIDU_MAP_GEOCONV_API = "http://api.map.baidu.com/geocoder/v2/?";

//    @Value("${baidu.map.service.ak}")
//    private String mapAk;

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
            supportAreaMap.put(supportAreaEntity.getAreaId(), supportAreaEntity);
        }

        for (SupportAreaEntity supportAreaEntity : allCity) {
            if (supportAreaEntity.getLevel() == SysConstant.AreaLevel.CITYPREFIX.getValue()) {
                supportAreaListWrapper.add(supportAreaEntity);

            } else {
                if (supportAreaEntity.getLevel() != SysConstant.AreaLevel.REGION.getValue()) {
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
        SupportAreaEntity       cityEntity          = findByCnName(cityName);
        List<SupportAreaEntity> allRegionEntityList = supportAreaRepository.findByParentId(cityEntity.getAreaId());

        List<String> regionArray = new ArrayList<>();
        for (SupportAreaEntity supportAreaEntity : allRegionEntityList) {
            regionArray.add(supportAreaEntity.getCnName());
        }
        return regionArray;
    }

    @Override
    public List<SupportAreaEntity> findByLevel(int level) {
        return supportAreaRepository.findByLevel(level);
    }

    @Override
    public SupportAreaEntity findByCnName(String cnName) {
        return supportAreaRepository.findByCnName(cnName);
    }

    @Override
    public SupportAreaEntity findByAreaId(Long areaId) {
        return supportAreaRepository.findOne(areaId);
    }

    @Transactional
    @Override
    public void save(List<SupportAreaEntity> areaEntityList) {
        supportAreaRepository.save(areaEntityList);
    }

    @Override
    public BaiduMapLocation getBaiduMapLocation(String city, String region, String address, String community) {
        return null;
//        String encodeAddress = city + region + address;
//        if (StringUtils.isNotBlank(community)) {
//            encodeAddress += community;
//        }
//        String encodeCity;
//
//        try {
//            encodeCity = URLEncoder.encode(city, "utf-8");
//            encodeAddress = URLEncoder.encode(encodeAddress, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            logger.error("Error to encode house address {}", e);
//            return null;
//        }

//        StringBuilder sb = new StringBuilder(BAIDU_MAP_GEOCONV_API);
//        sb.append("address=").append(encodeAddress)
//                .append("&city=").append(encodeCity)
//                .append("&output=json")
//                .append("&ak=").append(mapAk);

//        HttpGet httpGet = new HttpGet(sb.toString());
//        try {
//            HttpResponse httpResponse = httpClient.execute(httpGet);
//            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                logger.warn("Can not get baidu map location");
//                return null;
//            }
//
//            String result = EntityUtils.toString(httpResponse.getEntity());
//            JsonNode jsonNode = objectMapper.readTree(result);
//
//            int status = jsonNode.get("status").asInt();
//            if (status != 0) {
//                logger.warn("Error to get map location for status : {}", status);
//                return null;
//            } else {
//                JsonNode jsonLocation = jsonNode.get("result").get("location");
//                return new BaiduMapLocation(jsonLocation.get("lng").asDouble(), jsonLocation.get("lat").asDouble());
//            }
//
//        } catch (IOException e) {
//            logger.error("Error to fetch baidumap api", e);
//            return null;
//        }
    }
}
