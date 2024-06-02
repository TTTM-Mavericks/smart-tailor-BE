package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
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

//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
//    private User user;

    private String action;

    @Column(name = "relation_id")
    private UUID relationID;

    private Boolean status;

    private String detail;
}
