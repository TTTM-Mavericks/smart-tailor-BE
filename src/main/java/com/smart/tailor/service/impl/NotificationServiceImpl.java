package com.smart.tailor.service.impl;

import com.smart.tailor.config.DataHandler;
import com.smart.tailor.entities.Notification;
import com.smart.tailor.mapper.NotificationMapper;
import com.smart.tailor.repository.NotificationRepository;
import com.smart.tailor.service.NotificationService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.request.NotificationRequest;
import com.smart.tailor.utils.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends TextWebSocketHandler implements NotificationService {
    private final DataHandler dataHandler;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;


    @Override
    public void sendGlobalNotification(NotificationRequest notificationRequest) throws Exception {
        dataHandler.sendGlobal(notificationRequest);
        var userList = userService.getAllUserResponse();
        for (var user : userList) {
            notificationRequest.setRecipient(user.getUserID());
            saveNotification(notificationRequest);
        }
    }

    @Override
    public void sendPrivateNotification(NotificationRequest notificationRequest) throws Exception {
        dataHandler.sendToUser(notificationRequest.getRecipient(), notificationRequest);
        saveNotification(notificationRequest);
    }

    @Override
    public void saveNotification(NotificationRequest notificationRequest) throws Exception {
        notificationRepository.save(Notification.builder().action(notificationRequest.getType()).user(userService.getUserByUserID(notificationRequest.getRecipient()).get()).userID(notificationRequest.getRecipient()).status(false).detail(notificationRequest.getMessage()).build());
    }

    @Override
    public List<NotificationResponse> getNotificationByUserID(String userID) throws Exception {
        return notificationRepository.findAll().stream().filter(n -> n.getUserID().equals(userID)).map(notificationMapper::mapToNotificationResponse).toList();
    }
}