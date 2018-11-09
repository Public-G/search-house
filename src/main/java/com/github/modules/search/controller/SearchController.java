package com.github.modules.search.controller;

import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.CustomResponseWrapper;
import com.github.common.utils.PageUtils;
import com.github.modules.data.entity.SupportAreaEntity;
import com.github.modules.data.service.SupportAreaService;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.search.form.HouseForm;
import com.github.modules.search.service.SearchService;
import com.github.modules.search.utils.ConditionRangeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 房源搜索
 *
 * @author ZEALER
 * @date 2018-11-4
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private SupportAreaService supportAreaService;

    @GetMapping("/city")
    @ResponseBody
    public ApiResponse getCity() {
        List<SupportAreaEntity> allCity = supportAreaService.findAllCity();
        return ApiResponse.ofSuccess().put("data", allCity);
    }

    @GetMapping("/condition")
    @ResponseBody
    public ApiResponse getCondition() {
        Map<String, ConditionRangeUtils> priceBlock = ConditionRangeUtils.PRICE_BLOCK;
        Map<String, ConditionRangeUtils> areaBlock  = ConditionRangeUtils.AREA_BLOCK;
        return ApiResponse.ofSuccess()
                .put("priceBlock", priceBlock)
                .put("areaBlock", areaBlock);
    }

    @GetMapping("/search")
    public String rent(HouseForm houseForm, Model model) {

        List<String> regions = supportAreaService.findRegionByCity(houseForm.getCityCnName());
        model.addAttribute("regions", regions);

        PageUtils pageBean = searchService.query(houseForm);
        model.addAttribute("houses", pageBean);

        if (StringUtils.isBlank(houseForm.getRegionCnName())) {
            houseForm.setRegionCnName("*");
        }
        houseForm.setLimit(pageBean.getData().size());
        houseForm.setPriceBlock(ConditionRangeUtils.matchPrice( houseForm.getPriceBlock() ).getKey() );
        houseForm.setAreaBlock(ConditionRangeUtils.matchArea( houseForm.getAreaBlock() ).getKey() );

        model.addAttribute("searchBody", houseForm);

        return "front/list";
    }


    @GetMapping("/search/{id:[A-Za-z0-9]{32}}")
    public String detail(@PathVariable String id, Model model) {
        HouseDTO house = searchService.queryById(id);
        model.addAttribute("house", house);
        return "front/detail";
    }


//    @GetMapping("/search")
//    @ResponseBody
//    public ApiResponse search(HouseForm houseForm, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PageUtils pageBean = searchService.query(houseForm);
//
//        request.setAttribute("house", pageBean.getData());
//        String houseResult = getHtmlOutput(request, response);
//
//        return ApiResponse.ofSuccess().put("data", houseResult.substring(houseResult.indexOf("<body>") + 6, houseResult.indexOf("</body>")));
//    }

    /**
     * 将渲染后的html返回
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    private String getHtmlOutput(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomResponseWrapper wrapper = new CustomResponseWrapper(response);
        request.getRequestDispatcher("/template/houseResult").include(request, wrapper);
        return wrapper.getContent();
    }

}
