package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Notification;
import com.smart.tailor.repository.NotificationRepository;
import com.smart.tailor.service.BrandService;
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
    private final BrandService brandService;

    @Override
    public void sendGlobalNotification(NotificationRequest notificationRequest) {
        messagingTemplate.convertAndSend("/topic/global-notifications", notificationRequest);
    }

    @Override
    public void sendPrivateNotification(NotificationRequest notificationRequest) throws Exception {
        messagingTemplate.convertAndSendToUser(
                notificationRequest.getRecipient(),
                "/topic/private-notifications",
                notificationRequest
        );
        saveNotification(notificationRequest);
    }

    @Override
    public void saveNotification(NotificationRequest notificationRequest) throws Exception {
        if (notificationRequest.getType().equals("BRAND REGISTRATION")) {
            var brand = brandService.getBrandByEmail(notificationRequest.getSender());
            notificationRepository.save(
                    Notification
                            .builder()
                            .action(notificationRequest.getType())
                            .userID(brand.getBrandID())
                            .status(false)
                            .detail(notificationRequest.getMessage())
                            .build()
            );
        }
    }
}
