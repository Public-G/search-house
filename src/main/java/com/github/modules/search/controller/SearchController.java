package com.github.modules.search.controller;

import com.github.common.constant.SysConstant;
import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.CustomResponseWrapper;
import com.github.common.utils.PageUtils;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.service.RuleService;
import com.github.modules.data.service.SpiderService;
import com.github.modules.search.constant.HouseIndexConstant;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.search.dto.HouseListDTO;
import com.github.modules.search.entity.CollectEntity;
import com.github.modules.search.entity.UserEntity;
import com.github.modules.search.form.HouseForm;
import com.github.modules.search.service.CollectService;
import com.github.modules.search.service.SearchService;
import com.github.modules.search.utils.ConditionRangeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 房源搜索
 *
 * @author ZEALER
 * @date 2018-11-04
 */
@RequestMapping("/rent")
@Controller
public class SearchController extends AbstractController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private SpiderService spiderService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private CollectService collectService;

    @GetMapping("/city")
    @ResponseBody
    public ApiResponse getCity() {
        List<String> allCity = spiderService.findCity();
        return ApiResponse.ofSuccess().put("data", allCity);
    }

    /**
     * 搜索建议
     *
     * @param prefix
     * @return
     */
    @GetMapping("/autocomplete")
    @ResponseBody
    public ApiResponse autocomplete(@RequestParam String prefix) {
        if (StringUtils.isBlank(prefix)) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.BAD_REQUEST);
        }

        Set<String> suggest = searchService.suggest(prefix);
        return ApiResponse.ofSuccess().put("data", suggest);
    }


    /**
     * 房源搜索
     *
     * @param houseForm
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String search(@Valid HouseForm houseForm, BindingResult bindingResult, Model model) {

        if (bindingResult.getErrorCount() > 0) {
            return "error/404";
        }

        // 房源信息
        PageUtils pageBean = searchService.query(houseForm);
        model.addAttribute("houses", pageBean);

        Long userId = getUserId();
        // 房源收藏数
        if (userId != null) {
            int count = collectService.findCount(userId);
            model.addAttribute("collectCount", count);
        }

        houseForm.setLimit(pageBean.getData().size());
        houseForm.setPriceBlock(ConditionRangeUtils.matchPrice(houseForm.getPriceBlock()).getKey());
        houseForm.setSquareBlock(ConditionRangeUtils.matchArea(houseForm.getSquareBlock()).getKey());

        model.addAttribute("searchBody", houseForm);

        return "front/list";
    }


    /**
     * 房源详情
     *
     * @param houseId
     * @param model
     * @return
     */
    @GetMapping("/search/{houseId:[A-Za-z0-9]{32}}")
    public String detail(@PathVariable String houseId, Model model) {
        HouseDTO house = searchService.queryById(houseId);
        model.addAttribute("house", house);
        return "front/detail";
    }

    /**
     * 房源来源
     */
    @GetMapping("/loadWebsite")
    @ResponseBody
    public ApiResponse loadWebsite() {
        List<RuleEntity> allRule = ruleService.findAll();
        return ApiResponse.ofSuccess().put("data", allRule);
    }

    /**
     * 是否收藏
     *
     * @param houseId
     * @return
     */
    @GetMapping("/collect/{houseId:[A-Za-z0-9]{32}}")
    @ResponseBody
    public ApiResponse isCollect(@PathVariable String houseId) {
        Long userId = getUserId();

        checkUser(userId);

        CollectEntity collectEntity = collectService.findByUserIdAndhouseId(userId, houseId);
        if (collectEntity == null) {
            return ApiResponse.ofFail("未收藏");
        }

        return ApiResponse.ofSuccess();
    }

    /**
     * 房源收藏
     *
     * @param houseId
     * @return
     */
    @PostMapping("/collect/save")
    @ResponseBody
    public ApiResponse collect(@RequestParam String houseId) {
        Long userId = getUserId();

        checkUser(userId);

        CollectEntity collectEntity = collectService.findByUserIdAndhouseId(userId, houseId);
        if (collectEntity != null) {
            return ApiResponse.ofFail("已收藏，刷新查看");
        }

        collectService.save(userId, houseId);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除收藏
     *
     * @param houseId
     * @return
     */
    @DeleteMapping("/collect/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam String houseId) {
        Long userId = getUserId();

        checkUser(userId);

        collectService.delete(userId, houseId);

        return ApiResponse.ofSuccess();
    }

    /**
     * 查看收藏
     */
    @GetMapping("/contactHouse")
    @ResponseBody
    public ApiResponse contactHouse() {
        Long userId = getUserId();

        checkUser(userId);

        return ApiResponse.ofSuccess();
    }

    /**
     * 跳转收藏页
     */
    @GetMapping("/collectList")
    public String collectList(Model model) {
        Long userId = getUserId();

        if (userId != null) {
            List<HouseListDTO> houseListDTOList = searchService.queryById(userId);
            int count = collectService.findCount(userId);

            model.addAttribute("collect", houseListDTOList);
            model.addAttribute("collectCount", count);
        }

        return "front/contactHouse";
    }

    private void checkUser(Long userId) {
        if (userId == null) {
            throw new SHException(ApiResponse.ResponseStatus.UNAUTHORIZED.getCode(),
                    ApiResponse.ResponseStatus.UNAUTHORIZED.getStandardMessage());
        }
    }

    private Long getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserEntity) {
            UserEntity userEntity = (UserEntity) principal;
            return userEntity.getUserId();
        }

        return null;
    }
}
