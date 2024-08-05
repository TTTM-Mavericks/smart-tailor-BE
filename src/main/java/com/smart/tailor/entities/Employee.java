package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


@Entity
@Table(name = "employee")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee extends AuditEntity implements Serializable {
    @Id
    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "employee_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "total_task")
    private Integer totalTask = 0;

    @Column(name = "pending_task")
    private Integer pendingTask = 0;

    @Column(name = "success_task")
    private Integer successTask = 0;

    @Column(name = "fail_task")
    private Integer failTask = 0;

    @PrePersist
    private void prePersist() {
        this.employeeID = Utilities.generateCustomPrimaryKey();
    }
}
