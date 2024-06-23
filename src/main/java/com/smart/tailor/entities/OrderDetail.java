package com.smart.tailor.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail extends AuditEntity implements Serializable {
    @Id
    @Column(name = "order_detail_id", nullable = false, unique = true)
    @UuidGenerator
    private UUID orderDetailID;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false, unique = false)
    private Order orderID;

    private Integer quantity;

    private String size;
}
