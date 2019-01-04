package com.github.modules.data.controller;

import com.github.common.annotation.SysLog;
import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.entity.SpiderEntity;
import com.github.modules.data.pojo.HouseIndexTemplate;
import com.github.modules.data.constant.CommunicateConstant;
import com.github.modules.data.service.HouseService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 房源管理
 *
 * @author ZEALER
 * @date 2018-11-01
 */
@RequestMapping("/data/house")
@Controller
public class HouseController {

    @Autowired
    private HouseService houseService;

    /**
     * 列表/添加 页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/data/house/house" + forwardType;
    }

    /**
     * 所有房源数据列表
     *
     * @param pageForm 请求参数
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm) {
        PageUtils pageBean = houseService.findPage(pageForm);
        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 房源信息
     */
    @GetMapping("/info/{sourceUrlId}")
    public String info(@PathVariable String sourceUrlId, Model model){
        model.addAttribute("house", houseService.findById(sourceUrlId));

        return "admin/data/house/houseInfo";
    }

    /**
     * 修改房源信息
     */
    @SysLog("修改房源")
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("house") HouseIndexTemplate houseIndexTemplate){
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (houseIndexTemplate == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        ValidatorUtils.validateEntity(houseIndexTemplate);

        houseService.update(houseIndexTemplate);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除房源信息
     */
    @SysLog("删除房源")
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam("selectIds") String[] sourceUrlIds) {
        houseService.delete(sourceUrlIds);

        return ApiResponse.ofSuccess();
    }

    @ModelAttribute
    private void customModelAttribute(@RequestParam(value = "sourceUrlId", required = false) String sourceUrlId,
                                      Model model) {
        if (StringUtils.isNotBlank(sourceUrlId)) {
            model.addAttribute("house", houseService.findById(sourceUrlId));
        }
    }
}
