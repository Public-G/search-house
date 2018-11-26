package com.github.modules.search.controller;

import com.github.common.constant.SysConstant;
import com.github.modules.search.dto.HouseBucketDTO;
import com.github.modules.search.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 地图找房
 *
 * @author ZEALER
 * @date 2018-11-20
 */
@RequestMapping("/rent")
@Controller
public class MapController {

    @Autowired
    private MapService mapService;

    @RequestMapping("/map")
    public String map(@RequestParam(required = false, defaultValue = SysConstant.DEFAULT_CITY) String city,
                      Model model) {
        List<HouseBucketDTO> aggData = mapService.mapAggsByCity(city);

        model.addAttribute("city", city);
        model.addAttribute("aggData", aggData);
        return "front/map";
    }
}
