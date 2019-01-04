package com.github.modules.search.service;

import com.github.modules.search.dto.IndexChartDTO;
import com.github.modules.search.dto.IndexDTO;

/**
 * 首页
 *
 * @author ZEALER
 * @date 2018-12-02
 */
public interface IndexService {

    IndexDTO fetchRealtimeData(String city);

    IndexChartDTO fetchRentTrend(String city);

    IndexChartDTO fetchRentTop();

    IndexChartDTO fetchRentRegion(String city);

    IndexChartDTO fetchRentTypePie(String city);

    IndexChartDTO fetchRentRentPie(String city);

    IndexChartDTO fetchRegionPie(String city);

}
