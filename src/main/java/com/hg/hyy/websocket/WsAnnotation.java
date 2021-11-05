package com.hg.hyy.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@Component
@ServerEndpoint(value = "/spring2.ws")
public class WsAnnotation {
    private static final Set<WsAnnotation> connections = new CopyOnWriteArraySet<WsAnnotation>();
    private Session session;

    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
        String message = String.format("%s %s", session.getId(), "has joined.");
        broadcast(message);
    }

    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("%s %s", session.getId(), "has disconnected.");
        broadcast(message);
    }

    @OnMessage
    public void incoming(String message) {
        String msg = String.format("%s %s %s", session.getId(), "said:", message);
        broadcast(msg);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
    }

    private static void broadcast(String msg) {
        for (WsAnnotation client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                }
                String message = String.format("%s %s", client.session.getId(), "has been disconnected.");
                broadcast(message);
            }
        }
    }
}
