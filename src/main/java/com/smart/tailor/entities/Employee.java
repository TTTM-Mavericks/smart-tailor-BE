package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

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
    private Integer employeeID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "employee_id", referencedColumnName = "user_id")
    private User user;

    private Integer totalTask;

    private Integer pendingTask;

    private Integer successTask;

    private Integer failTask;
}
