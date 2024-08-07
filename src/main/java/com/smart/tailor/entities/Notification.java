package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


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
    @Column(name = "notification_id", columnDefinition = "varchar(14)")
    private String notificationID;

    @Column(columnDefinition = "varchar(100)")
    private String action;

    @Column(name = "user_id", columnDefinition = "varchar(14)")
    private String userID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", columnDefinition = "varchar(14)", nullable = false, insertable = false, updatable = false)
    private User user;

    private Boolean status;

    @Column(columnDefinition = "varchar(255)")
    private String detail;

    @PrePersist
    private void prePersist() {
        this.notificationID = Utilities.generateCustomPrimaryKey();
    }
}
