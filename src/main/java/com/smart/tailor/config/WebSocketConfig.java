package com.smart.tailor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getUserHandShakeHandler(), "/websocket")
                .setAllowedOrigins("*");
    }

    @Bean
    DataHandler getUserHandShakeHandler() {
        return new DataHandler();
    }
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/websocket")
//                .setAllowedOriginPatterns("*")
//                .setHandshakeHandler(new UserHandshakeHandler())
//                .withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/ws");
//        registry.enableSimpleBroker("/topic");
//    }
}
