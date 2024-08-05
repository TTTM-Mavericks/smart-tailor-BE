package com.smart.tailor.entities;

import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


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
    @Column(name = "stage_id", updatable = false, nullable = false)
    String stageId;

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

    @PrePersist
    private void prePersist() {
        this.stageId = Utilities.generateCustomPrimaryKey();
    }
}
