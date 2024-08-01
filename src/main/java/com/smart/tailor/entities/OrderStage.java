package com.smart.tailor.entities;

import com.smart.tailor.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "order_stage")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class OrderStage extends AuditEntity implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "stage_id", updatable = false, nullable = false)
    UUID stageId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage", nullable = false)
    OrderStatus stage;

    @Column(name = "current_quantity")
    Integer currentQuantity;

    @Column(name = "remaining_quantity")
    Integer remainingQuantity;

    Boolean status;
}
