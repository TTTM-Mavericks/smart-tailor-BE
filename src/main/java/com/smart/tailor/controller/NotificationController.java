package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.NotificationService;
import com.smart.tailor.utils.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstant.NotificationAPI.Notification)
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping(APIConstant.NotificationAPI.SEND_PUBLIC_NOTIFICATION)
    public ResponseEntity<ObjectNode> sendGlobalNotification(@RequestBody NotificationRequest notificationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            notificationService.sendGlobalNotification(notificationRequest);
            respon.put("status", 200);
            respon.put("message", MessageConstant.SEND_NOTIFICATION_SUCCESSFULLY);
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN SEND PUBLIC NOTIFICATION. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.NotificationAPI.SEND_NOTIFICATION)
    public ResponseEntity<ObjectNode> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            notificationService.sendPrivateNotification(notificationRequest);
            respon.put("status", 200);
            respon.put("message", MessageConstant.SEND_NOTIFICATION_SUCCESSFULLY);
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN SEND NOTIFICATION. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }
}
