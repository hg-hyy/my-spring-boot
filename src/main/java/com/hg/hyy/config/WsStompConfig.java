package com.hg.hyy.config;

import com.hg.hyy.Interceptors.WsChannelInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket相关配置基于stomp
 *
 * @author hyy
 * @date 2021-11-05
 * @since 1.0.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WsStompConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private WsChannelInterceptor wsChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp.ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 客户端需要把消息发送到/message/xxx地址
        registry.setApplicationDestinationPrefixes("/message");
        // 服务端广播消息的路径前缀，客户端需要相应订阅/topic/yyy这个地址的消息
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(wsChannelInterceptor);
    }

}