package com.smart.tailor.entities;

import com.smart.tailor.enums.TypeOfReport;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
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

    @ManyToOne
    @JoinColumn(name = "reported_user_id", referencedColumnName = "user_id")
    private User reportedUser;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @Column(name = "report_status")
    private Boolean reportStatus;

    @Column(name = "type_of_report")
    private TypeOfReport typeOfReport;

    private String content;

    @Column(name = "report_date_time")
    private LocalDateTime reportDateTime;
}
