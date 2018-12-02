package com.github.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * WebSocket 配置
 *
 * @see EnableWebSocketMessageBroker : 开启使用STOMP协议来传输基于代理（MessageBroker）的消息
 * @author ZEALER
 * @date 2018-11-30
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    /**
     * 注册端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //注册一个Stomp的节点（endpoint），并指定使用SockJS协议
        stompEndpointRegistry.addEndpoint("/endpointWisely").withSockJS();
    }

    /**
     * 配置消息代理
     * 定义消息代理，通俗一点讲就是设置消息连接请求的各种规范信息
     * 启动Broker，消息的发送的地址符合配置的前缀来的消息才发送到这个broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 表示客户端订阅地址的前缀信息，也就是客户端接收服务端消息的地址的前缀信息
        registry.enableSimpleBroker("/topic");
    }
}
