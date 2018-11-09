package com.github.modules.data.service.impl;

import com.github.common.utils.PageUtils;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.data.service.HouseService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.common.constant.SysConstant.RequestParam.CURR;
import static com.github.common.constant.SysConstant.RequestParam.LIMIT;

@Service("houseService")
public class HouseServiceImpl implements HouseService {

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PageUtils findPage(Map<String, String> params) {
        Integer     curr     = Integer.valueOf(params.get(CURR.getName()));
        Integer     limit    = Integer.valueOf(params.get(LIMIT.getName()));

        SearchResponse searchResponse = esClient.prepareSearch(HouseIndexConstant.INDEX_NAME)
                .setTypes(HouseIndexConstant.TYPE_NAME)
                .setFrom(curr - 1)
                .setSize(limit)
                .get();

        SearchHit[] hits = searchResponse.getHits().getHits();

        List<HouseDTO> dataList = new ArrayList<>();
        for (SearchHit hit : hits) {
            HouseDTO houseDTO = modelMapper.map(hit.getSource(), HouseDTO.class);
            houseDTO.setId(hit.getId());

            dataList.add(houseDTO);
        }

        return new PageUtils(searchResponse.getHits().getTotalHits(), dataList);
    }
}
