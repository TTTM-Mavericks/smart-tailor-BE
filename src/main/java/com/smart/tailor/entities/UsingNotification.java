package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "using_notification")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsingNotification {
    @Id
    @UuidGenerator
    @Column(name = "using_notification_id", nullable = false, unique = true)
    private UUID usingNotificationID;

    @Column(name = "notification_id", insertable = false, updatable = false)
    private UUID notificationID;

    @ManyToOne
    @JoinColumn(name = "notification_id", unique = false, nullable = false)
    private Notification notification;

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userID;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = false, nullable = false)
    private User user;
}
