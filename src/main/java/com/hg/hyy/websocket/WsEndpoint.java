package com.hg.hyy.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;

public class WsEndpoint extends Endpoint {

    private static final Set<WsEndpoint> connections = new CopyOnWriteArraySet<>();
    private Session session;

    private static class ChatMessageHandler implements MessageHandler.Partial<String> {
        private Session session;

        private ChatMessageHandler(Session session) {
            this.session = session;
        }

        @Override
        public void onMessage(String message, boolean last) {
            String msg = String.format("%s %s %s", session.getId(), "said:", message);
            broadcast(msg);
        }
    };

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        connections.add(this);
        this.session.addMessageHandler(new ChatMessageHandler(session));
        String message = String.format("%s %s", session.getId(), "has joined.");
        broadcast(message);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        connections.remove(this);
        String message = String.format("%s %s", session.getId(), "has disconnected.");
        broadcast(message);
    }

    @Override
    public void onError(Session session, Throwable throwable) {
    }

    private static void broadcast(String msg) {
        for (WsEndpoint client : connections) {
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
