package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report extends AuditEntity implements Serializable {
    @Id
    @Column(name = "report_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID reportID;

    @Column(name = "type_of_report")
    private String typeOfReport;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = true, unique = false)
    private Order order;

    private String content;

    @Column(name = "report_status")
    private Boolean reportStatus;
}
