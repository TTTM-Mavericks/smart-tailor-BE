package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Task extends AuditEntity implements Serializable {
    @Id
    @Column(name = "task_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID taskID;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "user_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    private String detail;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "task_status")
    private Boolean taskStatus;

    private String title;
}
