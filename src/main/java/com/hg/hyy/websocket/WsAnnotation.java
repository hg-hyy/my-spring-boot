package com.hg.hyy.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hg.hyy.entity.Greeting;
import com.hg.hyy.entity.HelloMessage;
import com.hg.hyy.entity.Ws;

/**
 * 
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 *                 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * 
 */

@Component
@ServerEndpoint("/annotation.ws")
public class WsAnnotation {

    private static Logger log = LoggerFactory.getLogger(WsAnnotation.class);
    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static CopyOnWriteArraySet<WsAnnotation> webSocketSet = new CopyOnWriteArraySet<WsAnnotation>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 
     * 连接建立成功调用的方法
     * 
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * 
     */

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this); // 加入set中
        addOnlineCount(); // 在线客户端数加1
        String message = String.format("%s %s", session.getId(), "has joined.");
        broadcast(message);
        log.error("有新连接加入!当前在线客户端数为：" + getOnlineCount());
        try {
            // sendMessageObj("连接成功");
            sendMessageObj(new Ws("连接成功", 1000, ""));
        } catch (

        Exception e) {
            log.error("websocket IO异常");
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        try {
            webSocketSet.remove(this); // 从set中删除
            subOnlineCount(); // 在线数减1
            session.close();
            log.error("有一连接关闭！当前在线人数为" + getOnlineCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * 
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.error("来自客户端的消息:" + message);
        Map<String, Object> maps = new HashMap<>();
        maps.put("type", message);
        sendInfo(maps);
    }

    /**
     * 群发自定义消息
     */
    public void sendInfo(Object obj) {
        for (WsAnnotation item : webSocketSet) {
            try {
                item.sendMessageObj(obj);
            } catch (Exception e) {
                continue;
            }

        }
    }

    /**
     * 
     * 发生错误时调用
     * 
     * @param session
     * @param error
     * 
     */

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * 
     * @param message
     * 
     * @throws IOException
     * 
     */

    /**
     * 实现服务器主动推送
     */

    public void sendMessageString(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendMessageObj(Object obj) {
        try {
            synchronized (this.session) {
                this.session.getBasicRemote().sendText((JSON.toJSONString(obj)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void broadcast(String msg) {
        for (WsAnnotation client : webSocketSet) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                webSocketSet.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                }
                String message = String.format("%s %s", client.session.getId(), "has been disconnected.");
                broadcast(message);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WsAnnotation.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WsAnnotation.onlineCount--;
    }
}
