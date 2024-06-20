package com.smart.tailor.service;

import com.smart.tailor.utils.request.NotificationRequest;

public interface NotificationService {
    void sendGlobalNotification(NotificationRequest notificationRequest);

    void sendPrivateNotification(NotificationRequest notificationRequest);

    void saveNotification(NotificationRequest notificationRequest);
}
