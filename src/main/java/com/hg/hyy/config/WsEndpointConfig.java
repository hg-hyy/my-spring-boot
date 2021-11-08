package com.hg.hyy.config;

import java.util.HashSet;
import java.util.Set;
import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import com.hg.hyy.websocket.WsEndpoint;
import org.springframework.context.annotation.Configuration;

/**
 * WebSocket相关配置基于编程javax.websocket
 *
 * @author hyy
 * @date 2021-11-05
 * @since 1.0.0
 */

@Configuration
public class WsEndpointConfig implements ServerApplicationConfig {

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
        return null;
    }

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> scanned) {
        Set<ServerEndpointConfig> result = new HashSet<ServerEndpointConfig>();
        if (scanned.contains(WsEndpoint.class)) {
            result.add(ServerEndpointConfig.Builder.create(WsEndpoint.class, "/endpoint.ws").build());

        }
        return result;
    }
}