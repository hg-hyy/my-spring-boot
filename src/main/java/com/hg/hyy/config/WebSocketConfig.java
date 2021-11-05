package com.hg.hyy.config;

import com.hg.hyy.Interceptors.WebSocketInterceptor;
import com.hg.hyy.utils.MyMessageHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket相关配置基于spring.websocket
 *
 * @author hyy
 * @date 2021-11-05
 * @since 1.0.0
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public WebSocketHandler myHandler() {
        return new MyMessageHandler();
    }

    /**
     * 注册handle
     * 
     * @see org.springframework.web.socket.config.annotation.WebSocketConfigurer#registerWebSocketHandlers(org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry)
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/spring.ws").addInterceptors(new WebSocketInterceptor());
        registry.addHandler(myHandler(), "/socketJs/spring.ws").addInterceptors(new WebSocketInterceptor())
                .withSockJS();

    }
}
