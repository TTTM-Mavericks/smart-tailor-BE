package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailKey implements Serializable {
    @Column(name = "design_id", nullable = false)
    private UUID designID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id", nullable = false, insertable = false, updatable = false)
    private Design design;

    @Column(name = "order_id", nullable = false)
    private UUID orderID;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false, insertable = false, updatable = false)
    private Order order;
}
