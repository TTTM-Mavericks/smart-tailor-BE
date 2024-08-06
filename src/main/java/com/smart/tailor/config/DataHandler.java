package com.smart.tailor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class DataHandler extends TextWebSocketHandler {
    private final Logger LOG = LoggerFactory.getLogger(DataHandler.class);

    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String query = String.valueOf(session.getUri());
        String email = "";
        if (query != null) {
            String[] pair = query.split("=");
            email = pair[1];
        }
        LOG.info("User with email '{}' opened the page", email);
//        LOG.info("User with email '{}' opened the page", message.getPayload());
    }
}
