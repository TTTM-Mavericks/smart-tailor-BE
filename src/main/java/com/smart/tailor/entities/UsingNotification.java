package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;



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
    @Column(name = "using_notification_id", columnDefinition = "varchar(14)")
    private String usingNotificationID;

    @ManyToOne
    @JoinColumn(name = "notification_id", unique = false, nullable = false)
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = false, nullable = false)
    private User user;

    @PrePersist
    private void prePersist() {
        this.usingNotificationID = Utilities.generateCustomPrimaryKey();
    }
}
