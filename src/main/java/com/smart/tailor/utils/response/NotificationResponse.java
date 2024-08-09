package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private String notificationID;
    private String action;
    private String userID;
    private Boolean status;
    private String detail;
    private String createDate;
    private String lastModifiedDate;
}
