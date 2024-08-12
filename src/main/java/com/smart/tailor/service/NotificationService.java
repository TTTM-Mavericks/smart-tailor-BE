package com.smart.tailor.service;

import com.smart.tailor.utils.request.NotificationRequest;
import com.smart.tailor.utils.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void sendGlobalNotification(NotificationRequest notificationRequest) throws Exception;

    void sendPrivateNotification(NotificationRequest notificationRequest) throws Exception;

    void saveNotification(NotificationRequest notificationRequest) throws Exception;

    List<NotificationResponse> getNotificationByUserID(String jwtToken, String userID) throws Exception;
}
