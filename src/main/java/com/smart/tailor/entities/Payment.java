package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends AuditEntity implements Serializable {
    @Id
    @Column(name = "payment_id")
    @UuidGenerator
    private UUID paymentID;

    @Column(name = "relation_id")
    private UUID relationID;

    @Column(name = "status")
    private Boolean status;

    private Double amount;

    private String method;

    @Column(name = "payment_goods")
    private String paymentGoods;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;
}
