package com.github.modules.data.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.modules.data.pojo.HouseIndexTemplate;
import com.github.modules.data.service.BaiduMapService;
import com.github.modules.data.service.HouseService;
import com.github.modules.search.constant.HouseIndexConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 同步Elasticsearch和LBS中的房源数据，以Elasticsearch的为准
 *
 * @author ZEALER
 * @date 2018-12-09
 */
@Component
public class SnycHouseTask {

    @Autowired
    private BaiduMapService baiduMapService;

    @Autowired
    private HouseService houseService;

    public void syncHouse() {
        Set<String> poiIds = getPoiIds();
        Set<String> esIds = getEsIds();

        Assert.notNull(poiIds, "poi数据集不能为空");
        Assert.notNull(esIds, "es数据集不能为空");

        Set<String> poiContains = new HashSet<>();
        for (String esId : esIds) {
            if (poiIds.contains(esId)) {
                poiContains.add(esId); // 收集poi中存在且es中也存在的数据
                poiIds.remove(esId); // 收集poi中存在而在es中不存在的数据
            }
        }

        // 收集poi中不存在而在es中存在的数据
        for (String poiContain : poiContains) {
            if (esIds.contains(poiContain)) {
                esIds.remove(poiContain);
            }
        }

        // 按照id删除poi列表中的数据
        if (poiIds.size() > 0) {
            removeLBS(poiIds);
        }

        // 将es中存在而poi中不存在的数据上传到LBS
        if (esIds.size() > 0) {
            uploadLBS(esIds);
        }
    }

    private Set<String> getPoiIds() {
        Set<String> sourceUrlIds = new HashSet<>();
        return getLBSList(sourceUrlIds, 0);
    }

    private Set<String> getLBSList(Set<String> sourceUrlIds, int page_index) {
        JsonNode lbsList = baiduMapService.getLBSList(page_index, 200); //分页数目，上限为200

        JsonNode pois = lbsList.get("pois");
        if (!(pois instanceof NullNode)) {
            for (JsonNode jsonNode : pois) {
                String sourceUrlId = jsonNode.get(HouseIndexConstant.SOURCE_URL_ID).asText();
                sourceUrlIds.add(sourceUrlId);
            }

            page_index += 1;
            getLBSList(sourceUrlIds, page_index); // 递归查询
        }

        return sourceUrlIds;
    }

    private Set<String> getEsIds() {
        return houseService.scrollGetIds();
    }

    private void removeLBS(Set<String> sourceUrlIds) {
//        StringBuilder sb = new StringBuilder();
//        int index = 0;
        for (String sourceUrlId : sourceUrlIds) {
            baiduMapService.removeLBS(sourceUrlId);
//            sb.append(sourceUrlId).append(",");
//            index += 1;
//            if (index == 1000) { // 最多1000个id
//                sb.append("#");
//                index = 0;
//            }
        }

//        String[] split = sb.toString().split("#");
//        for (String ids : split) {
//            baiduMapService.removeLBS(ids);
//        }
    }

    private void uploadLBS(Set<String> esIds) {
        String[] ids = esIds.toArray(new String[]{});

        List<HouseIndexTemplate> houseIndexTemplateList = houseService.multiGet(ids);

        for (HouseIndexTemplate houseIndexTemplate : houseIndexTemplateList) {
            baiduMapService.uploadLBS(houseIndexTemplate.getLocation(), houseIndexTemplate.getTitle(),
                    houseIndexTemplate.getAddress(), houseIndexTemplate.getSourceUrlId(),
                    houseIndexTemplate.getPrice(), houseIndexTemplate.getSquare());
        }
    }

}
