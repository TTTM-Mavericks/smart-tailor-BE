package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "notification")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends AuditEntity implements Serializable {
    @Id
    @Column(name = "notification_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID notificationID;

    @Column(columnDefinition = "varchar(100)")
    private String action;

    @Column(name = "user_id")
    private UUID userID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    private Boolean status;

    @Column(columnDefinition = "varchar(255)")
    private String detail;
}
