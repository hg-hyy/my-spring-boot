package com.hg.hyy.config;

import lombok.Getter;
import lombok.Setter;

import java.net.ConnectException;

import com.hg.hyy.mqtt.MqttPub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Classname MtqqEntity
 * @Description mqtt相关配置信息
 * @Date 2021-10-28
 * @Created by hyy
 */
@Component
@ConfigurationProperties("spring.mqtt")
@Setter
@Getter
public class MqttConfig {
    @Autowired
    private MqttPub mqttPub;

    // 连接地址
    private String hostUrl;
    private String clientId;
    private String username;
    private String password;
    private int timeout;
    private int keepalive;
    // 默认连接话题
    private String defaultTopic;

    @Bean
    public MqttPub getMqttPub() throws ConnectException {
        mqttPub.connect(hostUrl, clientId, username, password, timeout, keepalive);

        // 以/#结尾表示订阅所有以test开头的主题
        mqttPub.subscribe("test/#", 0);
        return mqttPub;
    }
}
