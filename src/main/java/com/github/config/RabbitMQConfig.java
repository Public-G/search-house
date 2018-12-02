package com.github.config;

import com.github.modules.data.constant.CommunicateConstant;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * RabbitMQ 配置
 *
 * @author ZEALER
 * @date 2018-11-15
 */
@EnableRabbit
@Configuration
public class RabbitMQConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @PostConstruct
    public void init() {
        // 创建持久化的队列，分别处理数据和命令
        amqpAdmin.declareQueue(new Queue(CommunicateConstant.RABBITMQ_HOUSE_QUEUE, true));
        amqpAdmin.declareQueue(new Queue(CommunicateConstant.RABBITMQ_COMMAND_QUEUE, true));

        // 创建两个持久化的交换器
        amqpAdmin.declareExchange(new DirectExchange(CommunicateConstant.RABBITMQ_HOUSE_EXCHANGE,
                true, false));
        amqpAdmin.declareExchange(new DirectExchange(CommunicateConstant.RABBITMQ_COMMAND_EXCHANGE,
                true, false));

        // 队列通过路由键绑定到交换器
        amqpAdmin.declareBinding(new Binding(CommunicateConstant.RABBITMQ_HOUSE_QUEUE, Binding.DestinationType.QUEUE,
                CommunicateConstant.RABBITMQ_HOUSE_EXCHANGE, CommunicateConstant.RABBITMQ_HOUSE_QUEUE, null));
        amqpAdmin.declareBinding(new Binding(CommunicateConstant.RABBITMQ_COMMAND_QUEUE, Binding.DestinationType.QUEUE,
                CommunicateConstant.RABBITMQ_COMMAND_EXCHANGE, CommunicateConstant.RABBITMQ_COMMAND_QUEUE, null));
    }

    /**
     * 使用json序列化消息
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
