package com.github.modules.search.controller;

import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import com.github.modules.data.service.SpiderService;
import com.github.modules.search.dto.IndexChartDTO;
import com.github.modules.search.dto.IndexDTO;
import com.github.modules.search.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 首页
 *
 * @author ZEALER
 * @date 2018-12-02
 */
@Controller
public class IndexController extends AbstractController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/")
    public String index(@RequestParam(name = "city", required = false,
                                      defaultValue = SysConstant.DEFAULT_CITY) String city, Model model) {
        IndexDTO indexDTO = indexService.fetchRealtimeData(city);
        indexDTO.setCity(city);
        model.addAttribute("data", indexDTO);
        return "front/index";
    }

    /**
     * 租金走势
     * @param city
     * @return
     */
    @GetMapping("/rentTrend")
    @ResponseBody
    public ApiResponse rentTrend(@RequestParam(name = "city", required = false,
            defaultValue = SysConstant.DEFAULT_CITY) String city) {
        IndexChartDTO indexChartDTO = indexService.fetchRentTrend(city);
        return ApiResponse.ofSuccess().put("data", indexChartDTO);
    }


    /**
     * 全国城市租金Top10
     * @return
     */
    @GetMapping("/rentTop")
    @ResponseBody
    public ApiResponse rentTop() {
        IndexChartDTO indexChartDTO = indexService.fetchRentTop();
        return ApiResponse.ofSuccess().put("data", indexChartDTO);
    }

    /**
     * 各区房源租金
     * @param city
     * @return
     */
    @GetMapping("/rentRegion")
    @ResponseBody
    public ApiResponse rentRegion(@RequestParam(name = "city", required = false,
            defaultValue = SysConstant.DEFAULT_CITY) String city) {
        IndexChartDTO indexChartDTO = indexService.fetchRentRegion(city);
        return ApiResponse.ofSuccess().put("data", indexChartDTO);
    }

    /**
     * 户型分布
     * @param city
     * @return
     */
    @GetMapping("/rentTypePie")
    @ResponseBody
    public ApiResponse rentTypePie(@RequestParam(name = "city", required = false,
            defaultValue = SysConstant.DEFAULT_CITY) String city) {
        IndexChartDTO indexChartDTO = indexService.fetchRentTypePie(city);
        return ApiResponse.ofSuccess().put("data", indexChartDTO);
    }

    /**
     * 租金分布
     * @param city
     * @return
     */
    @GetMapping("/rentRentPie")
    @ResponseBody
    public ApiResponse rentRentPie(@RequestParam(name = "city", required = false,
            defaultValue = SysConstant.DEFAULT_CITY) String city) {
        IndexChartDTO indexChartDTO = indexService.fetchRentRentPie(city);
        return ApiResponse.ofSuccess().put("data", indexChartDTO);
    }

    /**
     * 区域分布
     * @param city
     * @return
     */
    @GetMapping("/rentRegionPie")
    @ResponseBody
    public ApiResponse rentRegionPie(@RequestParam(name = "city", required = false,
            defaultValue = SysConstant.DEFAULT_CITY) String city) {
        IndexChartDTO indexChartDTO = indexService.fetchRegionPie(city);
        return ApiResponse.ofSuccess().put("data", indexChartDTO);
    }

}
