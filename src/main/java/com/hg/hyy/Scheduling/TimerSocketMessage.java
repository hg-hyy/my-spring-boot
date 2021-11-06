package com.hg.hyy.Scheduling;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import com.hg.hyy.websocket.WsAnnotation;

@Component
@EnableScheduling
public class TimerSocketMessage {

    /**
     * 推送消息到前台
     */
    @Scheduled(cron = "*/5 * * * * * ")
    public void SocketMessage() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("type", "sendMessage");
        maps.put("data", "ok");
        new WsAnnotation().sendInfo(maps);
    }
}
