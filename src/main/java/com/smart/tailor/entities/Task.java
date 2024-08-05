package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task extends AuditEntity implements Serializable {
    @Id
    @Column(name = "task_id", unique = true, nullable = false)
    private String taskID;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employeeID;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order orderID;

    @Column(columnDefinition = "varchar(255)")
    private String detail;

    @Column(name = "task_status")
    private Boolean taskStatus;

    @Column(columnDefinition = "varchar(100)")
    private String title;

    @PrePersist
    private void prePersist() {
        this.taskID = Utilities.generateCustomPrimaryKey();
    }
}
