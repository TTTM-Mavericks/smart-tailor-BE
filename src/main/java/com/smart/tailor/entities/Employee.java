package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "employee")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Employee extends AuditEntity implements Serializable {
    @Id
    @Column(name = "employee_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID employeeID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "employee_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "total_task")
    private Integer totalTask;

    @Column(name = "pending_task")
    private Integer pendingTask;

    @Column(name = "success_task")
    private Integer successTask;

    @Column(name = "fail_task")
    private Integer failTask;
}
