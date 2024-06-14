package com.smart.tailor.config;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String userID = null;
        try {
            String query = request.getURI().getQuery();
            if (query != null) {
                String[] pair = query.split("=");
                userID = pair[1];
            }
            LOG.info("User with ID '{}' opened the page", userID);

        } catch (Exception ex) {
            logger.error("ERROR IN USER HANDSHAKE HANDLER: {}", ex);
        }
        return new UserPrincipal(userID);
    }
}