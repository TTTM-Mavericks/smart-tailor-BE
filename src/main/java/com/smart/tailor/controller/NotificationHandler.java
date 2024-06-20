package com.smart.tailor.controller;

import com.smart.tailor.service.NotificationService;
import com.smart.tailor.utils.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationHandler {
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String getMessage(final NotificationRequest message) throws InterruptedException {
        notificationService.sendGlobalNotification(message);
        return message.getMessage();
    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public String getPrivateMessage(final NotificationRequest message) throws Exception {
        Thread.sleep(1000);
        notificationService.sendPrivateNotification(message);
        return message.getMessage();
    }
}
