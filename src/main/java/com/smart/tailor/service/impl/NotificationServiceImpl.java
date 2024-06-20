package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Notification;
import com.smart.tailor.repository.NotificationRepository;
import com.smart.tailor.service.NotificationService;
import com.smart.tailor.utils.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendGlobalNotification(NotificationRequest notificationRequest) {
        messagingTemplate.convertAndSend("/topic/global-notifications", notificationRequest);
    }

    @Override
    public void sendPrivateNotification(NotificationRequest notificationRequest) {
        messagingTemplate.convertAndSendToUser(
                notificationRequest.getRecipient().toString(),
                "/topic/private-notifications",
                notificationRequest
        );
        saveNotification(notificationRequest);
    }

    @Override
    public void saveNotification(NotificationRequest notificationRequest) {
        notificationRepository.save(
                Notification
                        .builder()
                        .action(notificationRequest.getType())
                        .relationID(notificationRequest.getSender())
                        .status(false)
                        .detail(notificationRequest.getMessage())
                        .build()
        );
    }
}
