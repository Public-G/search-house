package com.github.modules.search.controller;

import com.github.common.constant.SysConstant;
import com.github.modules.data.service.SpiderService;
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
public class MapController extends AbstractController {

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private MapService mapService;

    @RequestMapping("/map")
    public String map(@RequestParam(name = "city", required = false,
                                    defaultValue = SysConstant.DEFAULT_CITY) String city,Model model) {

        model.addAttribute("city", city);

        List<HouseBucketDTO> aggData = mapService.mapAggsByCity(city);
        model.addAttribute("aggData", aggData);

        return "front/map";
    }
}
