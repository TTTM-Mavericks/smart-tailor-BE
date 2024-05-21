package com.smart.tailor.entities;

import com.smart.tailor.enums.TypeOfReport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Report extends AuditEntity implements Serializable {
    @Id
    @Column(name = "report_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportID;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "base_user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "report_status")
    private Boolean reportStatus;

    private TypeOfReport typeOfReport;

    private String content;

    @Column(name = "report_date_time")
    private LocalDateTime reportDateTime;
}
