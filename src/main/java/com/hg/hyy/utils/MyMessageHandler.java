package com.hg.hyy.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class MyMessageHandler implements WebSocketHandler {

    /**
     * userMap:使用线程安全map存储用户连接webscoket信息
     * 
     * @since JDK 1.7
     */
    private final static Map<String, WebSocketSession> userMap = new ConcurrentHashMap<String, WebSocketSession>();

    /**
     * 关闭websocket时调用该方法
     * 
     * @see org.springframework.web.socket.WebSocketHandler#afterConnectionClosed(org.springframework.web.socket.WebSocketSession,
     *      org.springframework.web.socket.CloseStatus)
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = this.getUserId(session);
        if (StringUtils.isNoneBlank(userId)) {
            userMap.remove(userId);
            System.err.println("用户" + userId + "已断开连接");
        } else {
            System.err.println("关闭时，获取用户id为空");
        }

    }

    /**
     * 建立websocket连接时调用该方法
     * 
     * org.springframework.web.socket.WebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = this.getUserId(session);
        if (StringUtils.isNoneBlank(userId)) {
            userMap.put(userId, session);
            session.sendMessage(new TextMessage("连接成功！"));
        }

    }

    /**
     * 客户端调用websocket.send时候，会调用该方法,进行数据通信
     * 
     * org.springframework.web.socket.WebSocketHandler#handleMessage(org.springframework.web.socket.WebSocketSession,
     * org.springframework.web.socket.WebSocketMessage)
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String msg = message.toString();
        String userId = this.getUserId(session);
        System.err.println("用户" + userId + "发送的消息是：" + msg);
        message = new TextMessage("服务端已经接收到消息，msg=" + msg);
        session.sendMessage(message);
    }

    /**
     * 传输过程出现异常时，调用该方法
     * 
     * org.springframework.web.socket.WebSocketHandler#handleTransportError(org.springframework.web.socket.WebSocketSession,
     * java.lang.Throwable)
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable e) throws Exception {
        WebSocketMessage<String> message = new TextMessage("异常信息：" + e.getMessage());
        session.sendMessage(message);
    }

    /**
     * 
     * org.springframework.web.socket.WebSocketHandler#supportsPartialMessages()
     */
    @Override
    public boolean supportsPartialMessages() {

        return false;
    }

    /**
     * sendMessageToUser:发给指定用户
     * 
     */
    public void sendMessageToUser(String userId, String contents) {
        WebSocketSession session = userMap.get(userId);
        if (session != null && session.isOpen()) {
            try {
                TextMessage message = new TextMessage(contents);
                session.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sendMessageToAllUsers:发给所有的用户
     * 
     */
    public void sendMessageToAllUsers(String contents) {
        Set<String> userIds = userMap.keySet();
        for (String userId : userIds) {
            this.sendMessageToUser(userId, contents);
        }
    }

    /**
     * getUserId:获取用户id
     * 
     * @author liuchao
     * @param session
     * @return
     * @since JDK 1.7
     */
    private String getUserId(WebSocketSession session) {
        try {
            String userId = (String) session.getAttributes().get("currentUser");
            return userId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
