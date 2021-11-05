package com.hg.hyy.config;

import java.util.HashSet;
import java.util.Set;
import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import com.hg.hyy.websocket.WsEndpoint;

public class WsEndpointConfig implements ServerApplicationConfig {

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
        return scanned;
    }

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> scanned) {
        Set<ServerEndpointConfig> result = new HashSet<ServerEndpointConfig>();
        if (scanned.contains(WsEndpoint.class)) {
            result.add(ServerEndpointConfig.Builder.create(WsEndpoint.class, "/spring.ws").build());
        }
        return result;
    }
}