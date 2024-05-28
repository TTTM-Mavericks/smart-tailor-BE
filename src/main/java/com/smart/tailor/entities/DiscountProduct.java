package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "discount_product")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiscountProduct extends AuditEntity implements Serializable {
    @Id
    @Column(name = "discount_product_id", unique = true, nullable = false)
    @UuidGenerator
    private UUID discountProductID;

    @Column(name = "discount_percent")
    private Double discountPercent;

    private Integer quantity;
}
