package com.smart.tailor.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail extends AuditEntity implements Serializable {
    @EmbeddedId
    private OrderDetailKey orderDetailKey;

    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "discount_product_id", referencedColumnName = "discount_product_id")
    private DiscountProduct discountProduct;
}
