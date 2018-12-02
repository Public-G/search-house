package com.github.modules.data.controller;

import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.modules.data.pojo.HouseIndexTemplate;
import com.github.modules.data.constant.CommunicateConstant;
import com.github.modules.data.service.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 具有缓存功能的线程池
     */
    private ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private HouseService houseService;

    @RabbitListener(queues = CommunicateConstant.RABBITMQ_HOUSE_QUEUE)
    public void listener(@Payload HouseIndexTemplate houseIndexTemplate, @Headers Map<String, Object> headers) {
        //任务开始时间
        long startTime = System.currentTimeMillis();

        executor.execute(() -> {
            long startTime2 = System.currentTimeMillis();
            try {
                ValidatorUtils.validateEntity(houseIndexTemplate);

//                houseService.saveOrUpdate(houseIndexTemplate);

                // 消息推送

                simpMessagingTemplate.convertAndSend("/topic/realTime", headers);
            } catch (SHException e) {
                logger.warn("房源数据 : {} 校验不通过, 原因 : {}", houseIndexTemplate.getSourceUrl(), e.getMsg());
            }
            long times2 = System.currentTimeMillis() - startTime2;
            logger.debug("子线程任务执行总时长 = {}", times2);
        });

        //任务执行总时长
        long times = System.currentTimeMillis() - startTime;

        logger.debug("主线程任务执行总时长 = {}", times);
    }

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
     * @param params 请求参数
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(@RequestParam Map<String, String> params) {
        PageUtils pageBean = houseService.findPage(params);
        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }
}
