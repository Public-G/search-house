package com.github.modules.data.listener;


import com.github.common.exception.SHException;
import com.github.common.utils.MapUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.modules.data.constant.CommunicateConstant;
import com.github.modules.data.pojo.HouseIndexTemplate;
import com.github.modules.data.service.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RabbitMQListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 具有缓存功能的线程池
     */
    private ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private HouseService houseService;

    /**
     * RabbitMQ消息监听
     *
     * @param houseIndexTemplate 消息体
     * @param headers 消息头
     */
    @RabbitListener(queues = CommunicateConstant.RABBITMQ_HOUSE_QUEUE)
    public void listener(@Payload HouseIndexTemplate houseIndexTemplate, @Headers Map<String, Object> headers) {
        executor.execute(() -> {
            long startTime2 = System.currentTimeMillis();
            try {
                ValidatorUtils.validateEntity(houseIndexTemplate);

                houseService.saveOrUpdate(houseIndexTemplate);

                // 消息推送
                simpMessagingTemplate.convertAndSend("/topic/realTime", headers);
            } catch (SHException e) {
                logger.warn("房源数据 : {} 校验不通过, 原因 : {}", houseIndexTemplate.getSourceUrl(), e.getMsg());

                // 删除系统中的该条数据
                houseService.delete(houseIndexTemplate.getSourceUrlId());
            }

            long times2 = System.currentTimeMillis() - startTime2;
            logger.debug("数据整合处理时长 = {}", times2);
        });
    }

//    @RabbitHandler
//    public void complete(byte[] message) {
//        // 消息推送
//        simpMessagingTemplate.convertAndSend("/topic/realTime", new MapUtils().put("code", 0));
////        Object code = content.get("code");
////        // 爬取完成
////        if (code != null) {
////
////        }
//    }
}
